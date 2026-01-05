package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neutron.viewmodel.employee.AddEmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    viewModel: AddEmployeeViewModel = viewModel(),
    onEmployeeSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Employee") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            /* ---------- Name ---------- */
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Name") },
                isError = uiState.nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.nameError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            /* ---------- Email ---------- */
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                isError = uiState.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            uiState.emailError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            /* ---------- Role ---------- */
            OutlinedTextField(
                value = uiState.role,
                onValueChange = viewModel::onRoleChange,
                label = { Text("Role") },
                modifier = Modifier.fillMaxWidth()
            )

            /* ---------- Department ---------- */
            OutlinedTextField(
                value = uiState.department,
                onValueChange = viewModel::onDepartmentChange,
                label = { Text("Department") },
                modifier = Modifier.fillMaxWidth()
            )

            /* ---------- Active ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Active")
                Switch(
                    checked = uiState.isActive,
                    onCheckedChange = viewModel::onActiveChange
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* ---------- Save Button ---------- */
            Button(
                onClick = {
                    viewModel.onSaveEmployee()
                    if (uiState.isSaveEnabled) {
                        onEmployeeSaved()
                    }
                },
                enabled = uiState.isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Employee")
            }
        }
    }
}
