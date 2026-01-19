package com.example.neutron.viewmodel.employee

data class AddEmployeeUiState(
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val department: String = "",
    val isActive: Boolean = true,
    val password: String = "", // Fixed: Reference for ViewModel

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null, // Fixed: Reference for ViewModel

    val isSaveEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)