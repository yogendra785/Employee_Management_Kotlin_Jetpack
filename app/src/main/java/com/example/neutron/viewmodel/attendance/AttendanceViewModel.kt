package com.example.neutron.viewmodel.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.domain.model.Attendance
import com.example.neutron.domain.model.AttendanceStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class AttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(startOfToday())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val attendanceList: StateFlow<List<Attendance>> =
        selectedDate
            .flatMapLatest { date ->
                repository.getAttendanceByDate(date)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun setDate(date: Long) {
        _selectedDate.value = date
    }

    fun markAttendance(employeeId: Long, status: AttendanceStatus) {
        viewModelScope.launch {
            val attendance = Attendance(
                employeeId = employeeId,
                date = _selectedDate.value,
                status = status
            )
            repository.markAttendance(attendance)
        }
    }

    fun getAttendanceHistory(employeeId: Long): Flow<List<Attendance>> {
        return repository.getAttendanceForEmployee(employeeId)
    }

    companion object {
        fun startOfToday(): Long {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }
    }
}