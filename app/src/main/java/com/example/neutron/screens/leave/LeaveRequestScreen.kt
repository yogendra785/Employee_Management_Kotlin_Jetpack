package com.example.neutron.screens.leave

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.leave.LeaveViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    viewModel: LeaveViewModel,
    onBack: () -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateRangePickerState = rememberDateRangePickerState()
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Apply for Leave", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))


        val startDateText = dateRangePickerState.selectedStartDateMillis?.let { formatter.format(Date(it)) } ?: "Start"
        val endDateText = dateRangePickerState.selectedEndDateMillis?.let { formatter.format(Date(it)) } ?: "End"

        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Selected Period: $startDateText to $endDateText",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select Dates")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = reason,
            onValueChange = { reason = it },
            label = { Text("Reason for Leave") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {

                viewModel.submitLeave(
                    employeeId = 123,
                    employeeName = "Current User",
                    startDate = dateRangePickerState.selectedStartDateMillis ?: 0L,
                    endDate = dateRangePickerState.selectedEndDateMillis ?: 0L,
                    reason = reason
                )
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = reason.isNotBlank() && dateRangePickerState.selectedEndDateMillis != null
        ) {
            Text("Submit Request")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.height(400.dp)
            )
        }
    }
}