package com.example.neutron.screens.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.AttendaceSummary
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.employee.EmployeeViewModel

@Composable
fun EmployeeDetailScreen(
    employeeId: Long,
    employeeViewModel: EmployeeViewModel,
    attendanceViewModel: AttendanceViewModel,
    onBack: () -> Unit
) {
    val employees by employeeViewModel.employees.collectAsState()
    val employee = employees.find { it.id == employeeId }

    // Using collectAsState ensures the UI refreshes when you mark attendance
    val summary by attendanceViewModel.getEmployeeSummary(employeeId)
        .collectAsState(initial = AttendaceSummary(0, 0, emptyList()))
     val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        employee?.let {
            Text(text = it.name, style = MaterialTheme.typography.headlineLarge)
            Text(text = it.department, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Quick Summary", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 RENAMED to EmployeeAttendanceStatsCard to fix the "Overload" error
            EmployeeAttendanceStatsCard(
                present = summary.totalPresent,
                absent = summary.totalAbsent
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Monthly History", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (summary.history.isEmpty()) {
                Text(text = "No history available", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
            } else {
                summary.history.forEach { stats ->
                    MonthlyHistoryItem(stats)
                }
            }
        }
    }
}

@Composable
fun EmployeeAttendanceStatsCard(present: Int, absent: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = present.toString(), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                Text(text = "Present", style = MaterialTheme.typography.labelLarge)
            }

            // The vertical line
            Box(modifier = Modifier.width(1.dp).height(40.dp).background(MaterialTheme.colorScheme.outlineVariant))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = absent.toString(), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.error)
                Text(text = "Absent", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun MonthlyHistoryItem(stats: com.example.neutron.domain.model.MonthlyStats) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stats.monthName, style = MaterialTheme.typography.bodyLarge)
            Row {
                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                    Text(text = "${stats.presentCount} P", modifier = Modifier.padding(4.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Badge(containerColor = MaterialTheme.colorScheme.errorContainer) {
                    Text(text = "${stats.absentCount} A", modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    }
}