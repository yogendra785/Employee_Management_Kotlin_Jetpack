package com.example.neutron.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employees.EmployeeScreen
import com.example.neutron.screens.attendance.AttendanceScreen

@Composable
fun NeutronNavHost(navController: NavHostController){
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {  paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.DASHBOARD,
            modifier= Modifier.padding(paddingValues)
        ){
            composable(NavRoutes.DASHBOARD){
                DashboardScreen(navController)
            }
            composable(NavRoutes.EMPLOYEE){
                EmployeeScreen()
            }
            composable(NavRoutes.ATTENDANCE){
                AttendanceScreen()
            }
        }
    }
}