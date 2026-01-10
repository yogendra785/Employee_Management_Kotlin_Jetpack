package com.example.neutron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel // 🔹 Use THIS import
import androidx.navigation.compose.rememberNavController
import com.example.neutron.navigation.NeutronNavHost
import com.example.neutron.ui.theme.NeutronTheme
import com.example.neutron.viewmodel.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint // 🔹 Added this

@AndroidEntryPoint // 🔹 Cjection for this Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NeutronTheme {
                val navController = rememberNavController()

                // 🔹 FIX: hiltViewModel() instead of viewModel()
                val authViewModel: AuthViewModel = hiltViewModel()

                NeutronNavHost(

                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }
    }
}