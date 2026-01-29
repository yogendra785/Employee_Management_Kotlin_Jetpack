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

    val employees: StateFlow<List<Employee>> =
        repository.getAllEmployees()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ✅ FIX: Changed function name and added null for the Uri
    fun insertEmployee(employee: Employee) {
        viewModelScope.launch {
            repository.insertEmployeeWithImage(employee, null)
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

    // ✅ FIX: Changed function name and added null for the Uri
    fun undoDelete() {
        recentlyDeletedEmployee?.let { employee ->
            viewModelScope.launch {
                repository.insertEmployeeWithImage(employee, null)
                recentlyDeletedEmployee = null
            }
        }
    }

    fun getEmployeeById(id: Long): Flow<Employee?> {
        return repository.getEmployeeById(id)
    }
}