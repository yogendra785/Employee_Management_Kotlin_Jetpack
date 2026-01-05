package com.example.neutron.data.repository

import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.mapper.toEmployee
import com.example.neutron.data.mapper.toEmployeeEntity
import com.example.neutron.domain.model.Employee
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EmployeeRepository(
    private val dao: EmployeeDao
) {

    // 🔹 Expose all employees as Flow (NOT StateFlow)
    fun getAllEmployees(): Flow<List<Employee>> {
        return dao.getAllEmployees()
            .map { entities ->
                entities.map { it.toEmployee() }
            }
    }

    // 🔹 Get single employee
    fun getEmployeeById(id: Long): Flow<Employee?> {
        return dao.getEmployeeById(id)
            .map { it?.toEmployee() }
    }

    // 🔹 Add employee
    suspend fun addEmployee(employee: Employee) {
        dao.insertEmployee(employee.toEmployeeEntity())
    }

    // 🔹 Update employee
    suspend fun updateEmployee(employee: Employee) {
        dao.updateEmployee(employee.toEmployeeEntity())
    }

    // 🔹 Delete employee
    suspend fun deleteEmployee(employee: Employee) {
        dao.deleteEmployee(employee.toEmployeeEntity())
    }

    // 🔹 Toggle active status
    suspend fun updateEmployeeStatus(
        id: Long,
        isActive: Boolean
    ) {
        dao.updateEmployeeStatus(id, isActive)
    }
}
