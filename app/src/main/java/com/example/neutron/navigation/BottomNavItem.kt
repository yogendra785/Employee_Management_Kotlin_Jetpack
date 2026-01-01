package com.example.neutron.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(
    val route: String,
    val title:String,
    val icon: ImageVector
){
    object Home :  BottomNavItem(
        route = NavRoutes.DASHBOARD,
        title = "Home",
        icon=Icons.Default.Home
    )

    object Employees : BottomNavItem(
        route= NavRoutes.EMPLOYEE,
        title = "Employees",
        icon = Icons.Default.People
    )

    object Attendance : BottomNavItem(
        route = NavRoutes.ATTENDANCE,
        title = "Attendance",
        icon = Icons.Default.CheckCircle
    )
}
