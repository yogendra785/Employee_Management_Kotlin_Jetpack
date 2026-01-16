package com.example.neutron.screens.leave

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.LeaveRequest
import com.example.neutron.viewmodel.leave.LeaveViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminLeaveListScreen(
    viewModel: LeaveViewModel,
    onBack: () -> Unit
) {
    // Observing all leaves from the database
    val allLeaves by viewModel.allLeaves.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Pending Requests", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Filter to show only PENDING requests for approval
            items(allLeaves.filter { it.status == "PENDING" }) { request ->
                AdminLeaveItem(
                    request = request,
                    onApprove = { viewModel.updateStatus(request, "APPROVED") },
                    onReject = { viewModel.updateStatus(request, "REJECTED") }
                )
            }
        }
    }
}

@Composable
fun AdminLeaveItem(
    request: LeaveRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(request.employeeName, style = MaterialTheme.typography.titleMedium)
                Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Text(request.status)
                }
            }
            Text("Reason: ${request.reason}", style = MaterialTheme.typography.bodyMedium)

            // Helper function for date formatting
            val dateText = "${formatDate(request.startDate)} - ${formatDate(request.endDate)}"
            Text("Dates: $dateText", style = MaterialTheme.typography.labelSmall)

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = onReject,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Reject")
                }
                Button(onClick = onApprove) {
                    Text("Approve")
                }
            }
        }
    }
}

// Simple date formatter helper
fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}