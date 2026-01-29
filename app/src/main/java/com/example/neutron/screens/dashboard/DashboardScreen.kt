package com.example.neutron.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(52.dp))

        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Neutron Management System",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            IconButton(
                onClick = { authViewModel.logout() },
                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Main Content Grid ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                DashboardCard(
                    title = "Daily Attendance",
                    subtitle = "Mark present/absent for today",
                    icon = Icons.Default.Today,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { navController.navigate(NavRoutes.ATTENDANCE) }
                )
            }

            val currentState = authState
            if (currentState is AuthState.Authenticated) {
                when (currentState.userRole) {
                    "ADMIN" -> {
                        item {
                            DashboardCard(
                                title = "Employees",
                                subtitle = "Manage Staff",
                                icon = Icons.Default.Groups,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                onClick = { navController.navigate(NavRoutes.EMPLOYEE) }
                            )
                        }
                        item {
                            DashboardCard(
                                title = "Approvals",
                                subtitle = "Review Leaves",
                                icon = Icons.AutoMirrored.Filled.FactCheck,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                onClick = { navController.navigate(NavRoutes.ADMIN_LEAVE_LIST) }
                            )
                        }
                        // 🔹 NEW: Salary Management Card for Admin
                        item(span = { GridItemSpan(2) }) {
                            DashboardCard(
                                title = "Salary Management",
                                subtitle = "Process monthly payroll",
                                icon = Icons.Default.Payments,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                onClick = { navController.navigate(NavRoutes.SALARY_MANAGEMENT) }
                            )
                        }
                    }
                    "EMPLOYEE" -> {
                        item {
                            DashboardCard(
                                title = "Leave",
                                subtitle = "Apply Now",
                                icon = Icons.Default.PostAdd,
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                onClick = { navController.navigate(NavRoutes.LEAVE_REQUEST) }
                            )
                        }
                        item {
                            DashboardCard(
                                title = "History",
                                subtitle = "View Status",
                                icon = Icons.Default.History,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                onClick = { navController.navigate(NavRoutes.MY_LEAVE_HISTORY) }
                            )
                        }
                    }
                }
            }
        }

        if (authState == AuthState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}