package com.example.neutron.viewmodel.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.domain.model.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddEmployeeViewModel @Inject constructor(private val repository: EmployeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmployeeUiState())
    val uiState: StateFlow<AddEmployeeUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(
            name = value,
            nameError = if (value.trim().length < 3) "Name too short" else null
        ) }
        updateSaveEnabledState()
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(
            email = value,
            emailError = if (!isValidEmail(value.trim())) "Invalid email format" else null
        ) }
        updateSaveEnabledState()
    }

    fun onRoleChange(value: String) {
        _uiState.update { it.copy(role = value) }
        updateSaveEnabledState()
    }

    fun onDepartmentChange(value: String) {
        _uiState.update { it.copy(department = value) }
    }

    fun onActiveChange(value: Boolean) {
        _uiState.update { it.copy(isActive = value) }
    }

    fun onSaveEmployee() {
        val currentState = _uiState.value
        if (!validate(currentState)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val emailExists = repository.isEmailExists(currentState.email.trim())
            if (emailExists) {
                _uiState.update { it.copy(emailError = "Email already registered", isLoading = false) }
                return@launch
            }

            val employee = Employee(
                name = currentState.name.trim(),
                email = currentState.email.trim(),
                role = currentState.role.trim(),
                department = currentState.department.trim(),
                isActive = currentState.isActive
            )

            repository.insertEmployee(employee)
            _uiState.update { it.copy(isSuccess = true, isLoading = false) }
        }
    }

    fun resetSuccess() {
        _uiState.update { AddEmployeeUiState() } // Fully reset for next entry
    }

    private fun updateSaveEnabledState() {
        _uiState.update { state ->
            state.copy(isSaveEnabled = state.name.isNotBlank() &&
                    state.email.isNotBlank() &&
                    state.role.isNotBlank() &&
                    state.nameError == null &&
                    state.emailError == null)
        }
    }

    private fun validate(state: AddEmployeeUiState): Boolean {
        val nameValid = state.name.trim().length >= 3
        val emailValid = isValidEmail(state.email.trim())
        return nameValid && emailValid && state.role.isNotBlank()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}