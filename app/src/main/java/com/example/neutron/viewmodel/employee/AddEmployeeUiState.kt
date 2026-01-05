package com.example.neutron.viewmodel.employee

data class AddEmployeeUiState (
    val name: String="",
    val email: String = "",
    val role: String = "",
    val department: String = "",
    val isActive: Boolean = true,

    val nameError: String? = null,
    val emailError: String? = null,

    val isSaveEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)