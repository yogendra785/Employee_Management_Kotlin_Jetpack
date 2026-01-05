package com.example.neutron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.neutron.data.local.database.EmployeeDatabase
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.navigation.NeutronNavHost
import com.example.neutron.ui.theme.NeutronTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ Create database & repository ONCE
        val database = EmployeeDatabase.getDatabase(applicationContext)
        val repository = EmployeeRepository(database.employeeDao())

        setContent {
            NeutronTheme {
                val navController = rememberNavController()

                // ✅ Pass repository, NOT ViewModel
                NeutronNavHost(
                    navController = navController,
                    employeeRepository = repository
                )
            }
        }
    }
}
