package com.example.neutron.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.AttendanceStatus
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    employeeViewModel: EmployeeViewModel,
    attendanceViewModel: AttendanceViewModel,
    userRole: String,
    onBackClick: () -> Unit = {} // Added for better navigation
) {
    val employees by employeeViewModel.employees.collectAsState()
    val attendanceList by attendanceViewModel.attendanceList.collectAsState()
    val selectedDate by attendanceViewModel.selectedDate.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daily Attendance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // --- Modern Date Selector ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                )
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
            }

            // --- Stats Summary (Optional Visual Boost) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${employees.size} Total Employees",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )
                val presentCount = attendanceList.count { it.status == AttendanceStatus.PRESENT }
                Text(
                    text = "Present: $presentCount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (employees.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Groups,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                        Text("No employees found", color = MaterialTheme.colorScheme.outline)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(employees, key = { it.id }) { employee ->
                        val attendance = attendanceList.find { it.employeeId == employee.id }

                        // Enhanced Attendance Item Card
                        AttendanceItemCard(
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
}

@Composable
fun AttendanceItemCard(
    name: String,
    status: AttendanceStatus?,
    isLocked: Boolean,
    isAdmin: Boolean,
    onPresent: () -> Unit,
    onAbsent: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isLocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Attendance Action Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (status == AttendanceStatus.PRESENT || (status == null && isAdmin)) {
                    FilterChip(
                        selected = status == AttendanceStatus.PRESENT,
                        onClick = onPresent,
                        label = { Text("Present") },
                        leadingIcon = if (status == AttendanceStatus.PRESENT) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        enabled = !isLocked || isAdmin,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFF2E7D32)
                        )
                    )
                }

                if (status == AttendanceStatus.ABSENT || (status == null && isAdmin)) {
                    FilterChip(
                        selected = status == AttendanceStatus.ABSENT,
                        onClick = onAbsent,
                        label = { Text("Absent") },
                        leadingIcon = if (status == AttendanceStatus.ABSENT) {
                            { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null,
                        enabled = !isLocked || isAdmin,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFF44336).copy(alpha = 0.2f),
                            selectedLabelColor = Color(0xFFD32F2F)
                        )
                    )
                }
            }
        }
    }
}