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

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {

                navController.navigate("main_app") {

                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate(NavRoutes.LOGIN) {
                    popUpTo(0)
                }
            }
            else -> {}
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