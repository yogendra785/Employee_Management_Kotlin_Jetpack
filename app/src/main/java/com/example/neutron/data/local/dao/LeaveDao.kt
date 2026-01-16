package com.example.neutron.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.neutron.domain.model.LeaveRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaveDao {
    @Insert
    suspend fun insertRequest(request: LeaveRequest)

    @Query("SELECT * FROM leave_requests ORDER BY requestDate DESC")
    fun getAllRequests(): Flow<List<LeaveRequest>>

    @Query("SELECT * FROM leave_requests WHERE employeeId = :empId")
    fun getRequestsByEmployee(empId: Long): Flow<List<LeaveRequest>>

    @Update
    suspend fun updateRequestStatus(request: LeaveRequest)
}