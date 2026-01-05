package com.example.neutron.viewmodel.employee

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddEmployeeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmployeeUiState())
    val uiState: StateFlow<AddEmployeeUiState> = _uiState.asStateFlow()

    /* -------------------- Input Handlers -------------------- */

    fun onNameChange(value: String) {
        val trimmed = value.trim()
        _uiState.value = _uiState.value.copy(
            name = trimmed,
            nameError = if (!isValidName(trimmed)) "Enter a valid name" else null
        )
        updateSaveEnabledState()
    }

    fun onEmailChange(value: String) {
        val trimmed = value.trim()
        _uiState.value = _uiState.value.copy(
            email = trimmed,
            emailError = if (!isValidEmail(trimmed)) "Invalid email address" else null
        )
        updateSaveEnabledState()
    }

    fun onRoleChange(value: String) {
        _uiState.value = _uiState.value.copy(role = value.trim())
        updateSaveEnabledState()
    }

    fun onDepartmentChange(value: String) {
        _uiState.value = _uiState.value.copy(department = value.trim())
    }

    fun onActiveChange(value: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = value)
    }

    /* -------------------- Save Action -------------------- */

    fun onSaveEmployee() {
        if (!validate()) return   // ⛔ FINAL SAFETY CHECK

        val state = _uiState.value

        // 🔜 Next step:
        // val employee = Employee(
        //     name = state.name,
        //     email = state.email,
        //     role = state.role,
        //     department = state.department,
        //     isActive = state.isActive
        // )
        //
        // repository.insertEmployee(employee)
    }

    /* -------------------- Validation -------------------- */

    private fun updateSaveEnabledState() {
        val state = _uiState.value
        _uiState.value = state.copy(
            isSaveEnabled =
                state.nameError == null &&
                        state.emailError == null &&
                        state.name.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.role.isNotBlank()
        )
    }

    private fun validate(): Boolean {
        val state = _uiState.value

        val nameError =
            if (!isValidName(state.name)) "Enter a valid name" else null

        val emailError =
            if (!isValidEmail(state.email)) "Invalid email address" else null

        val isValid =
            nameError == null &&
                    emailError == null &&
                    state.role.isNotBlank()

        _uiState.value = state.copy(
            nameError = nameError,
            emailError = emailError,
            isSaveEnabled = isValid
        )

        return isValid
    }

    /* -------------------- Validators -------------------- */

    private fun isValidName(name: String): Boolean {
        // Only letters and spaces, minimum 3 characters
        return name.matches(Regex("^[a-zA-Z ]{3,}$"))
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
