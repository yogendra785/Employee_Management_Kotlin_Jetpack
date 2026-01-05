package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.employee.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    viewModel: EmployeeViewModel,
    navigate: (String) -> Unit
) {
    val employees by viewModel.employees.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigate("add_employee")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Employee"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Employees (${employees.size})",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (employees.isEmpty()) {
                EmptyEmployeeState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(employees) { employee ->
                        EmployeeItem(
                            employee = employee,
                            onToggleActive = {
                                viewModel.toggleEmployeeStatus(employee)
                            },
                            onClick = {
                                navigate("employee_detail/${employee.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
