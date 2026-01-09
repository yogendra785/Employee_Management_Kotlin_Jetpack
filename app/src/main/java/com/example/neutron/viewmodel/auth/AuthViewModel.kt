package com.example.neutron.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
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

    // Reset state to idle (useful for clearing error messages in the UI)
    fun resetState() {
        _authState.value = if (repository.isLoggedIn()) AuthState.Authenticated else AuthState.Unauthenticated
    }
}