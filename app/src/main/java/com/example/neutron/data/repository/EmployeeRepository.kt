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

    fun getAllEmployees(): Flow<List<Employee>> {
        return dao.getAllEmployees()
            .map { entities ->
                entities.map { it.toEmployee() }
            }
    }

    fun getEmployeeById(id: Long): Flow<Employee?> {
        return dao.getEmployeeById(id)
            .map { it?.toEmployee() }
    }

    suspend fun insertEmployee(employee: Employee) {
        dao.insertEmployee(employee.toEmployeeEntity())
    }

    suspend fun updateEmployee(employee: Employee) {
        dao.updateEmployee(employee.toEmployeeEntity())
    }

    suspend fun deleteEmployee(employee: Employee) {
        dao.deleteEmployee(employee.toEmployeeEntity())
    }

    suspend fun updateEmployeeStatus(id: Long, isActive: Boolean) {
        dao.updateEmployeeStatus(id, isActive)
    }

    suspend fun isEmailExists(email: String): Boolean {
        // This now directly uses the Boolean check from our updated DAO
        return dao.isEmailExists(email)
    }
}