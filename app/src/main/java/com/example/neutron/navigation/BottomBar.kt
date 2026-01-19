package com.example.neutron.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavController,
    userRole: String // 🔹 Role is now a direct parameter
) {
    // 🔹 Re-calculate the list whenever userRole changes
    val items = remember(userRole) {
        val list = mutableListOf<BottomNavItem>()

        // Home is always visible
        list.add(BottomNavItem.Home)

        // 🔹 Show Employees ONLY if userRole is ADMIN
        if (userRole == "ADMIN") {
            list.add(BottomNavItem.Employees)
        }

        // Shared features
        list.add(BottomNavItem.Attendance)
        list.add(BottomNavItem.Profile)

        list
    }

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            // Highlight the icon if it matches the current route
            val isSelected = currentRoute == item.route ||
                (item.route == NavRoutes.DASHBOARD && currentRoute == null)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to dashboard to avoid stacking screens
                        popUpTo(NavRoutes.DASHBOARD) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}