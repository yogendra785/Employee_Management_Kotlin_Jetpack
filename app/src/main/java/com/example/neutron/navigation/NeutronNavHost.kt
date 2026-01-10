package com.example.neutron.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.neutron.screens.auth.LoginScreen
import com.example.neutron.screens.auth.SignupScreen
import com.example.neutron.screens.splash.SplashScreen
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.auth.AuthState

@Composable
fun NeutronNavHost(navController: NavHostController, authViewModel: AuthViewModel) {

    // 1. Observe the authentication state from the ViewModel
    val authState by authViewModel.authState.collectAsState()

    // 2. 🔹 CRITICAL: This reacts to state changes (like successful login)
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                // When state becomes Authenticated, jump to the main app
                navController.navigate("main_app") {
                    // Remove Login/Splash from the backstack so user can't go back to them
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                // If the user logs out, send them back to Login
                navController.navigate(NavRoutes.LOGIN) {
                    popUpTo(0)
                }
            }
            else -> {} // Do nothing for Loading or Error states here
        }
    }

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
            MainScaffold(
                rootNavController = navController,
                authViewModel = authViewModel
            )
        }
    }
}