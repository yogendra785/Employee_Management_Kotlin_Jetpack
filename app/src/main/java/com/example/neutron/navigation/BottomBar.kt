package com.example.neutron.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.neutron.viewmodel.auth.AuthViewModel

@Composable
fun BottomBar(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val userRole by authViewModel.userRole.collectAsState()

    val items = mutableListOf<BottomNavItem>()


    items.add(BottomNavItem.Home)


    if (userRole == "ADMIN") {
        items.add(BottomNavItem.Employees)
    }


    items.add(BottomNavItem.Attendance)
    items.add(BottomNavItem.Profile)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = currentRoute == item.route ||
                    (item.route == NavRoutes.DASHBOARD && currentRoute == null)

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true

                            restoreState = true
                        }
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