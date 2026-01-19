package com.example.neutron.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.local.entity.EmployeeEntity
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
    private val firestore: FirebaseFirestore
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

    fun signup(email: String, password: String, name: String, employeeId: String, role: String = "EMPLOYEE") {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).await()
                val uid = result.user?.uid

                if (uid != null) {
                    val userMap = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "employeeId" to employeeId,
                        "role" to role,
                        "createdAt" to System.currentTimeMillis()
                    )
                    firestore.collection("users").document(uid).set(userMap).await()
                    _authState.value = AuthState.Authenticated(role)
                } else {
                    _authState.value = AuthState.Error("Signup failed: Could not create user")
                }
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