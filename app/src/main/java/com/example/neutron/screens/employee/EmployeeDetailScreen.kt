package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.employee.EmployeeViewModel

@Composable
fun EmployeeDetailScreen(
    employeeId: Long,
    viewModel: EmployeeViewModel
) {
    // Observe employee list
    val employees by viewModel.employees.collectAsState()

    // Find employee by ID
    val employee = employees.find { it.id == employeeId }

    if (employee == null) {
        Text(
            text = "Employee Not Found",
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = employee.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Email: ${employee.email}")
        Text("Role: ${employee.role}")
        Text("Department: ${employee.department}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (employee.isActive)
                "Status: Active"
            else
                "Status: Inactive"
        )
    }
}
