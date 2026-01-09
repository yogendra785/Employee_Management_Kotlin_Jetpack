package com.example.neutron.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(NavRoutes.DASHBOARD, "Home", Icons.Default.Home)
    object Employees : BottomNavItem(NavRoutes.EMPLOYEE, "Staff", Icons.Default.People)
    object Attendance : BottomNavItem(NavRoutes.ATTENDANCE, "Check-in", Icons.Default.CheckCircle)
}