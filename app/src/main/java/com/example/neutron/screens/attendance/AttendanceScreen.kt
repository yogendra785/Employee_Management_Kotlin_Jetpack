package com.example.neutron.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.AttendanceStatus
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import java.util.Calendar

@Composable
fun AttendanceScreen(
    employeeViewModel: EmployeeViewModel,
    attendanceViewModel: AttendanceViewModel,
    userRole: String
) {
    val employees by employeeViewModel.employees.collectAsState()
    val attendanceList by attendanceViewModel.attendanceList.collectAsState()
    val selectedDate by attendanceViewModel.selectedDate.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Daily Attendance",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        AttendanceDateHeader(
            date = selectedDate,
            onPrevious = {
                val cal = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                attendanceViewModel.setDate(cal.timeInMillis)
            },
            onNext = {
                val cal = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                    add(Calendar.DAY_OF_YEAR, 1)
                }
                attendanceViewModel.setDate(cal.timeInMillis)
            },
            onToday = {
                attendanceViewModel.setDate(AttendanceViewModel.startOfToday())
            }
        )

        if (employees.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No employees found", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(employees, key = { it.id }) { employee ->
                    val attendance = attendanceList.find { it.employeeId == employee.id }

                    AttendanceItem(
                        name = employee.name,
                        status = attendance?.status,
                        isLocked = attendance != null,
                        isAdmin = userRole == "ADMIN",
                        onPresent = {
                            attendanceViewModel.markAttendance(employee.id, AttendanceStatus.PRESENT)
                        },
                        onAbsent = {
                            attendanceViewModel.markAttendance(employee.id, AttendanceStatus.ABSENT)
                        }
                    )
                }
            }
        }
    }
}