package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    // 🔹 Use Box to overlay FAB and Snackbar since we removed the inner Scaffold
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Staff Members (${employees.size})",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (employees.isEmpty()) {
                // Ensure this Composable exists in your employee package
                EmptyEmployeeState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
                ) {
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

        // 🔹 Manual Floating Action Button
        FloatingActionButton(
            onClick = { navigate(NavRoutes.ADD_EMPLOYEE) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Employee")
        }

        // 🔹 Manual Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}