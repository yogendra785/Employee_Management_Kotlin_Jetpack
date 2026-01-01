package com.example.neutron.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.local.entity.EmployeeEntity
import com.example.neutron.data.mapper.toEmployee
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.model.Employee
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EmployeeViewModel (
    private val repository: EmployeeRepository
): ViewModel(){
    val employees: StateFlow<List<Employee>> =
            repository.getAllEmployees()
                .map{entities ->
                    entities.map {it.toEmployee()}
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    emptyList()
                )

    //ADD EMPLOYEE

    fun addEmployee(
        name: String,
        email: String,
        role: String,
        department: String
    ){
        viewModelScope.launch {
            repository.insertEmployee(
                EmployeeEntity(
                    id= System.currentTimeMillis(),
                    name = name,
                    email = email,
                    role = role,
                    department = department,
                    isActive = true,
                    createdAt = System.currentTimeMillis()
            )
            )
        }
    }


    // TO REMOVE EMPLOYEE

    fun deleteEmployee(employee: Employee){
        viewModelScope.launch {
            repository.deleteEmployee(
                EmployeeEntity(
                    id = employee.id,
                    name = employee.name,
                    email = employee.email,

                    role = employee.role,
                    department = employee.department,
                    isActive = employee.isActive,
                    createdAt=0L
                )
            )
        }
    }


    //ACTIVATE / DEACTIVATE EMPLOYEE

    fun toggleEmployeeStatus(employee: Employee){
        viewModelScope.launch {
            repository.updateEmployeeStatus(
                employee.id,
                !employee.isActive
            )
        }
    }
}

