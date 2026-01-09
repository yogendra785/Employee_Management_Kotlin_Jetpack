package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.Attendance
import com.example.neutron.domain.model.AttendanceStatus
import com.example.neutron.utils.formatDate


@Composable
fun AttendanceHistoryItem(attendance: Attendance) {
    ListItem(
        headlineContent = { Text(formatDate(attendance.date)) },
        trailingContent = {
            Text(
                text = attendance.status.name,
                color = if (attendance.status == AttendanceStatus.PRESENT)
                    MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    )
}