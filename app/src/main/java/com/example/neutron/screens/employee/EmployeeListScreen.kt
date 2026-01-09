package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.navigation.NavRoutes
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import kotlinx.coroutines.launch

@Composable
fun EmployeeListScreen(
    viewModel: EmployeeViewModel,
    navigate: (String) -> Unit
) {
    val employees by viewModel.employees.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navigate(NavRoutes.ADD_EMPLOYEE) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Employee")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Staff Members (${employees.size})",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (employees.isEmpty()) {
                EmptyEmployeeState()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(employees, key = { it.id }) { employee ->
                        EmployeeItem(
                            employee = employee,
                            onToggleActive = { viewModel.toggleEmployeeStatus(employee) },
                            onDelete = {
                                viewModel.deleteEmployee(employee)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${employee.name} removed",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoDelete()
                                    }
                                }
                            },
                            onClick = {
                                navigate(NavRoutes.createDetailRoute(employee.id))
                            }
                        )
                    }
                }
            }
        }
    }
}