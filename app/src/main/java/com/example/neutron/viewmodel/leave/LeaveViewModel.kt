package com.example.neutron.viewmodel.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.LeaveRepository
import com.example.neutron.domain.model.LeaveRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveViewModel @Inject constructor(
    private val repository: LeaveRepository
): ViewModel(){

    val allLeaves = repository.getAllLeaves()
    fun updateStatus(request: LeaveRequest, newStatus: String){
        viewModelScope.launch {
            repository.updateLeaveStatus(request.copy(status = newStatus))
        }
    }

    //function that holds only the current user's leaves
    fun getMyLeaves(employeeId: Long): Flow<List<LeaveRequest>> {
        return repository.getEmployeeLeaves(employeeId)
    }


    fun submitLeave(
        employeeId: Long,
        employeeName: String,
        startDate: Long,
        endDate: Long,
        reason: String
    ){
        viewModelScope.launch {
            val newRequest = LeaveRequest(
                employeeId = employeeId,
                employeeName =  employeeName,
                startDate = startDate,
                endDate = endDate,
                reason = reason
            )
            repository.submitLeaveRequest(newRequest)
        }
    }
}