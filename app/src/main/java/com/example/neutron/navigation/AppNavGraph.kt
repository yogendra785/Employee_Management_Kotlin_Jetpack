package com.example.neutron.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.splash.SplashScreen

@Composable
fun AppNavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ){
        composable("splash") {
            SplashScreen()
        }
        composable("dashboard") {
            DashboardScreen(navController)

        }
    }
}