package com.example.neutron.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.neutron.screens.auth.LoginScreen
import com.example.neutron.screens.auth.SignupScreen
import com.example.neutron.screens.splash.SplashScreen
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.auth.AuthState

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onSignupClick = { navController.navigate(NavRoutes.SIGNUP) }
            )
        }

        composable(NavRoutes.SIGNUP) {
            SignupScreen(
                authViewModel = authViewModel,
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable("main_app") {
            val authState by authViewModel.authState.collectAsState()
            val role = (authState as? AuthState.Authenticated)?.userRole ?: "EMPLOYEE"

            MainScaffold(
                rootNavController = navController,
                authViewModel = authViewModel,
                userRole = role
            )
        }
    }
}