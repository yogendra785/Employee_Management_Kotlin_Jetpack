package com.example.neutron.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.neutron.navigation.NavRoutes
import com.example.neutron.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neutron HRM") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome back!", style = MaterialTheme.typography.headlineMedium)

            DashboardCard(
                title = "Manage Employees",
                subtitle = "View, add, or edit staff members",
                onClick = { navController.navigate(NavRoutes.EMPLOYEE) }
            )

            DashboardCard(
                title = "Daily Attendance",
                subtitle = "Mark present/absent for today",
                onClick = { navController.navigate(NavRoutes.ATTENDANCE) }
            )
        }
    }
}

@Composable
fun DashboardCard(title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
