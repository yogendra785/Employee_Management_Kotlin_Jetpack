package com.example.neutron.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.auth.AuthRepository
import com.example.neutron.data.local.entity.EmployeeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    // Role state
    private val _userRole = MutableStateFlow("EMPLOYEE")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    // Current Session State
    private val _currentUser = MutableStateFlow<EmployeeEntity?>(null)
    val currentUser: StateFlow<EmployeeEntity?> = _currentUser.asStateFlow()

    // Auth UI State
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    // 🔹 FIXED: Removed the stray '/' and added safety check
    fun loginUser(employee: EmployeeEntity) {
        _currentUser.value = employee
        _userRole.value = employee.role
    }

    fun setUserRole(role: String) {
        _userRole.value = role
    }

    private fun checkAuthState() {
        _authState.value = if (repository.isLoggedIn()) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState.Authenticated
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Login failed")
                }
            )
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.signup(email, password)
            result.fold(
                onSuccess = { _authState.value = AuthState.Authenticated },
                onFailure = { _authState.value = AuthState.Error(it.message ?: "Signup failed") }
            )
        }
    }

    // 🔹 UPDATED: Reset session data on logout
    fun logout() {
        repository.logout()
        _currentUser.value = null
        _userRole.value = "EMPLOYEE" // Reset to default
        _authState.value = AuthState.Unauthenticated
    }

    fun resetState() {
        checkAuthState()
    }
}