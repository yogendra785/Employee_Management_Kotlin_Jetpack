package com.example.neutron.viewmodel.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.domain.model.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val repository: EmployeeRepository
) : ViewModel() {

    private var recentlyDeletedEmployee: Employee? = null

    // 🔹 Convert cold flow to StateFlow for UI performance
    val employees: StateFlow<List<Employee>> =
        repository.getAllEmployees()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun insertEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.insertEmployee(employee)
        }
    }

    fun deleteEmployee(employee: Employee) {
        viewModelScope.launch {
            recentlyDeletedEmployee = employee
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

    fun undoDelete() {
        recentlyDeletedEmployee?.let { employee ->
            viewModelScope.launch {
                repository.insertEmployee(employee)
                recentlyDeletedEmployee = null
            }
        }
    }

    fun getEmployeeById(id: Long): Flow<Employee?> {
        return repository.getEmployeeById(id)
    }
}