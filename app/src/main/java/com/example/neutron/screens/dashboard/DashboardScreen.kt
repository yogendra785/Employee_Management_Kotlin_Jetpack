package com.example.neutron.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.neutron.navigation.NavRoutes
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.auth.AuthState

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // 🔹 Observe the entire authentication state
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Welcome back!", style = MaterialTheme.typography.headlineMedium)

            IconButton(onClick = { authViewModel.logout() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // 🔹 1. Shared Feature: Attendance (Visible to everyone)
        DashboardCard(
            title = "Daily Attendance",
            subtitle = "Mark present/absent for today",
            onClick = { navController.navigate(NavRoutes.ATTENDANCE) }
        )

        // 🔹 2. Handle different authentication states
        when (val state = authState) {
            is AuthState.Authenticated -> {
                // If authenticated, then check the role
                when (state.userRole) {
                    "ADMIN" -> {
                        // ADMIN ONLY: Staff Management
                        DashboardCard(
                            title = "Manage Employees",
                            subtitle = "View, add, or edit staff members",
                            onClick = { navController.navigate(NavRoutes.EMPLOYEE) }
                        )

                        // ADMIN ONLY: Leave Approval
                        DashboardCard(
                            title = "Review Leave Requests",
                            subtitle = "Approve or reject employee leaves",
                            onClick = { navController.navigate(NavRoutes.ADMIN_LEAVE_LIST) }
                        )
                    }
                    "EMPLOYEE" -> {
                        // EMPLOYEE ONLY: Requesting and History
                        DashboardCard(
                            title = "Leave Request",
                            subtitle = "Apply for time off",
                            onClick = { navController.navigate(NavRoutes.LEAVE_REQUEST) }
                        )

                        DashboardCard(
                            title = "My Leave History",
                            subtitle = "Check status of your request",
                            onClick = { navController.navigate(NavRoutes.MY_LEAVE_HISTORY) }
                        )
                    }
                }
            }
            AuthState.Loading -> {
                // Show a loader while role is being fetched
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AuthState.Error, AuthState.Unauthenticated -> {
                // For this screen, we can just show a loading state or nothing
                // as the user will likely be redirected away by the NavHost soon.
                // Or you can add a specific message.
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Improved readability
            )
        }
    }
}