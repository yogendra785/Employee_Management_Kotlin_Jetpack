package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.employee.AddEmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    viewModel: AddEmployeeViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate back automatically once the employee is saved in DB
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBack()
            viewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Employee") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Full Name") },
                isError = uiState.nameError != null,
                supportingText = { uiState.nameError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email Address") },
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.role,
                onValueChange = viewModel::onRoleChange,
                label = { Text("Job Role (e.g. Manager)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.department,
                onValueChange = viewModel::onDepartmentChange,
                label = { Text("Department") },
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Status", style = MaterialTheme.typography.titleSmall)
                        Text("Currently employed", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = uiState.isActive,
                        onCheckedChange = viewModel::onActiveChange
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onSaveEmployee() },
                enabled = uiState.isSaveEnabled && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Save Employee")
                }
            }
        }
    }
}