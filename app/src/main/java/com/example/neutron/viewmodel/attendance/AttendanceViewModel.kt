package com.example.neutron.viewmodel.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
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

    // 🔹 FIXED: Fetches the FULL history for the specific employee
    fun getEmployeeSummary(employeeId: Long): Flow<AttendaceSummary> {
        return repository.getAttendanceForEmployee(employeeId).map { records ->
            val totalP = records.count { it.status == AttendanceStatus.PRESENT }
            val totalA = records.count { it.status == AttendanceStatus.ABSENT }

            val sdf = SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault())

            val history = records.groupBy {
                val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                sdf.format(cal.time)
            }.map { (month, logs) ->
                MonthlyStats(
                    monthName = month,
                    presentCount = logs.count { it.status == AttendanceStatus.PRESENT },
                    absentCount = logs.count { it.status == AttendanceStatus.ABSENT }
                )
            }.sortedByDescending { it.monthName }

            AttendaceSummary(totalP, totalA, history)
        }
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