package com.example.neutron.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.auth.AuthState

@Composable
fun NeutronNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("main_app") {
                    popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
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

    RootNavigationGraph(navController = navController, authViewModel = authViewModel)
}