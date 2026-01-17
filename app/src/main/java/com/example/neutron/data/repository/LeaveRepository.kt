package com.example.neutron.data.repository


import com.example.neutron.data.local.dao.LeaveDao
import com.example.neutron.domain.model.LeaveRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LeaveRepository @Inject constructor(
    private val leaveDao: LeaveDao

){
    fun getAllLeaves(): Flow<List<LeaveRequest>> = leaveDao.getAllRequests()
    fun getLeavesForEmployee(empId: Long): Flow<List<LeaveRequest>> =
        leaveDao.getRequestsByEmployee(empId)

    //function to fetch leaves for specific employee
    fun getEmployeeLeaves(empId: Long): Flow<List<LeaveRequest>> =
        leaveDao.getRequestsByEmployee(empId)

    suspend fun submitLeaveRequest(request: LeaveRequest){
        leaveDao.insertRequest(request)
    }
    suspend fun updateLeaveStatus(request: LeaveRequest){
        leaveDao.updateRequestStatus(request)
    }

}
