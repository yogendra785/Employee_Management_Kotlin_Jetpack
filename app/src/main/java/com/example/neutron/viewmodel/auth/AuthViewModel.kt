package com.example.neutron.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.local.entity.EmployeeEntity
import com.example.neutron.domain.model.Employee
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val employeeRepository: com.example.neutron.data.repository.EmployeeRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<EmployeeEntity?>(null)
    val currentUser: StateFlow<EmployeeEntity?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(employeeId: String, passwordInput: String) {
        val cleanEmployeeId = employeeId.trim()
        val cleanPassword = passwordInput.trim()

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val users = firestore.collection("users").whereEqualTo("employeeId", cleanEmployeeId).get().await()
                if (users.isEmpty) {
                    _authState.value = AuthState.Error("User with this Employee ID not found")
                    return@launch
                }
                val user = users.documents.first()
                val email = user.getString("email")
                if (email == null) {
                    _authState.value = AuthState.Error("Email not found for this Employee ID")
                    return@launch
                }

                val result = firebaseAuth.signInWithEmailAndPassword(email, cleanPassword).await()
                val uid = result.user?.uid

                if (uid != null) {
                    fetchUserRoleAndAuthenticate(uid)
                } else {
                    _authState.value = AuthState.Error("Login failed: User not found")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Login Failed")
            }
        }
    }

    private suspend fun fetchUserRoleAndAuthenticate(uid: String) {
        try {
            val document = firestore.collection("users").document(uid).get().await()
            val role = document.getString("role") ?: "EMPLOYEE"
            _authState.value = AuthState.Authenticated(role)
            Log.d("AuthViewModel", "User Role Fetched: $role")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error fetching role", e)
            _authState.value = AuthState.Authenticated("EMPLOYEE")
        }
    }
    fun signup(email: String, password: String, name: String, employeeId: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // 1. Create user in Firebase
                val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).await()
                val uid = result.user?.uid ?: return@launch

                // 2. Save role in Firestore (Cloud)
                val userMap = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "role" to "EMPLOYEE",
                    "firebaseUid" to uid
                )
                firestore.collection("users").document(uid).set(userMap).await()

                // 3. Save basic info in Room (Local)
                val localEmployee = Employee(
                    id = 0, // Assuming 0 for auto-increment in Room
                    firebaseUid = uid,
                    name = name,
                    password = password,
                    email = email,
                    role = "EMPLOYEE",
                    department = "IT",
                    isActive = true, // Ensure this matches your Employee model
                    createdAt = System.currentTimeMillis(),
                    imagePath = null // Initial signup has no profile image path
                )

                // ✅ FIX: Use the new repository function and pass 'null' for the image Uri
                employeeRepository.insertEmployeeWithImage(localEmployee, null)

                _authState.value = AuthState.Authenticated("EMPLOYEE")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Signup failed")
            }
        }
    }
    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }
}