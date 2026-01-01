package com.example.neutron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.neutron.navigation.NeutronNavHost
import com.example.neutron.ui.theme.NeutronTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeutronTheme {
                val navController = rememberNavController()
                NeutronNavHost(navController)
            }
        }
    }
}