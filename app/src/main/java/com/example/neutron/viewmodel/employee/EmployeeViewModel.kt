package com.example.neutron.viewmodel.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.domain.model.Employee
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EmployeeViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    // 🔹 UI observes employees as StateFlow
    val employees: StateFlow<List<Employee>> =
        repository.getAllEmployees()
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5_000),
                emptyList()
            )

    fun addEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.addEmployee(employee)
        }
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.deleteEmployee(employee)
        }
    }

    fun toggleEmployeeStatus(employee: Employee) {
        viewModelScope.launch {
            repository.updateEmployeeStatus(
                id = employee.id,
                isActive = !employee.isActive
            )
        }
    }
}