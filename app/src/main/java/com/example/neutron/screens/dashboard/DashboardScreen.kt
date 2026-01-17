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

@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val userRole by authViewModel.userRole.collectAsState()

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

        // 1. Attendance Card
        DashboardCard(
            title = "Daily Attendance",
            subtitle = "Mark present/absent for today",
            onClick = { navController.navigate(NavRoutes.ATTENDANCE) }
        )



        // 2. Role-Based Cards
        if (userRole == "ADMIN") {
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
        } else {

            DashboardCard(
                title = "Leave Request",
                subtitle = "Apply for time off",
                onClick = { navController.navigate(NavRoutes.LEAVE_REQUEST) }
            )

            Spacer(modifier = Modifier.height(0.dp)) // Managed by spacedBy

            DashboardCard(
                title = "My Leave History",
                subtitle = "Check status of your request",
                onClick = { navController.navigate(NavRoutes.MY_LEAVE_HISTORY) }
            )
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