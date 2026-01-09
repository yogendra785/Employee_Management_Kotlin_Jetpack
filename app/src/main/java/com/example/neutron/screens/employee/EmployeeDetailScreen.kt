package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import com.example.neutron.viewmodel.attendance.AttendanceViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EmployeeDetailScreen(
    employeeId: Long,
    employeeViewModel: EmployeeViewModel,
    attendanceViewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    val employee by employeeViewModel.getEmployeeById(employeeId).collectAsState(initial = null)
    val attendanceHistory by attendanceViewModel.getAttendanceHistory(employeeId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        employee?.let { emp ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                item {
                    Text(emp.name, style = MaterialTheme.typography.headlineMedium)
                    Text(emp.email, style = MaterialTheme.typography.bodyLarge)
                    Text("Dept: ${emp.department}", style = MaterialTheme.typography.bodyMedium)
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text("Attendance History", style = MaterialTheme.typography.titleLarge)
                }

                items(attendanceHistory) { record ->
                    AttendanceHistoryItem(record)
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}