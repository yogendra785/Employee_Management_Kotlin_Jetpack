package com.example.neutron.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.auth.AuthRepository
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
    private val _userRole = MutableStateFlow("EMPLOYEE")
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    fun setUserRole(role:String){
        _userRole.value = role
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthState()
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
                onSuccess = { _authState.value = AuthState.Authenticated },
                onFailure = { _authState.value = AuthState.Error(it.message ?: "Login failed") }
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

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetState() {
        checkAuthState()
    }
}