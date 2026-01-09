package com.example.neutron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.neutron.navigation.NeutronNavHost
import com.example.neutron.ui.theme.NeutronTheme
import com.example.neutron.viewmodel.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge support for modern Android look
        enableEdgeToEdge()

        setContent {
            NeutronTheme {
                val navController = rememberNavController()

                // AuthViewModel is the source of truth for navigation (Login vs Main App)
                val authViewModel: AuthViewModel = viewModel()

                NeutronNavHost(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}