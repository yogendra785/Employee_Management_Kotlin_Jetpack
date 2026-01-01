package com.example.neutron.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController){
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Employees,
        BottomNavItem.Attendance
    )

    NavigationBar{
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute==item.route,
                onClick = {
                    navController.navigate(item.route){
                        popUpTo(NavRoutes.DASHBOARD)
                        launchSingleTop = true
                    }
                },
                icon={
                    Icon(item.icon, contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}