package com.example.neutron.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.neutron.screens.auth.LoginScreen
import com.example.neutron.screens.auth.SignupScreen
import com.example.neutron.viewmodel.auth.AuthState
import com.example.neutron.viewmodel.auth.AuthViewModel

@Composable
fun NeutronNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // Navigate to the main app container and clear auth history
                navController.navigate("main_app") {
                    popUpTo(0) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(NavRoutes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(authViewModel, onSignupClick = { navController.navigate(NavRoutes.SIGNUP) })
        }

        composable(NavRoutes.SIGNUP) {
            SignupScreen(authViewModel, onLoginClick = { navController.popBackStack() })
        }

        composable("main_app") {
            // We pass the root controller here so MainScaffold can trigger a logout redirect
            MainScaffold(
                rootNavController = navController,
                authViewModel = authViewModel
            )
        }
    }
}