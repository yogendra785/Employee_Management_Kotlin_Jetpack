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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    employeeViewModel: EmployeeViewModel,
    attendanceViewModel: AttendanceViewModel
) {
    val employees by employeeViewModel.employees.collectAsState()
    val attendanceList by attendanceViewModel.attendanceList.collectAsState()
    val selectedDate by attendanceViewModel.selectedDate.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daily Attendance") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
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
}