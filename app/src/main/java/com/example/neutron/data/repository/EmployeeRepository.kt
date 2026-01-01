package com.example.neutron.data.repository

import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow


class EmployeeRepository (
    private val employeeDao: EmployeeDao
){
    fun getAllEmployees(): Flow<List<EmployeeEntity>>{
        return employeeDao.getALLEmplpoyees()
    }

    suspend fun insertEmployee(employee: EmployeeEntity){
        employeeDao.insertEmployee(employee)
    }

    suspend fun deleteEmployee(employee: EmployeeEntity){
        employeeDao.deleteEmployee(employee)
    }

    suspend fun updateEmployeeStatus(
        employeeId: Long,
        active: Boolean
    ){
        employeeDao.updateEmployeeStatus(employeeId,active)
    }


}