package com.example.neutron.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neutron.screens.attendance.AttendanceScreen
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employee.*
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.employee.*

@Composable
fun MainScaffold(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {
    // This is the controller for the bottom navigation and internal app flow
    val appNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Pass the appNavController to the BottomBar
            BottomBar(navController = appNavController)
        }
    ) { paddingValues ->
        // The NavHost manages which screen content is shown
        NavHost(
            navController = appNavController,
            startDestination = NavRoutes.DASHBOARD,
            modifier = Modifier.padding(paddingValues)
        ) {
            // 1. Dashboard
            composable(NavRoutes.DASHBOARD) {
                DashboardScreen(
                    navController = appNavController,
                    authViewModel = authViewModel
                )
            }

            // 2. Employee List
            composable(NavRoutes.EMPLOYEE) {
                val vm: EmployeeViewModel = hiltViewModel()
                EmployeeListScreen(
                    viewModel = vm,
                    navigate = { route -> appNavController.navigate(route) }
                )
            }

            // 3. Attendance
            composable(NavRoutes.ATTENDANCE) {
                val eVM: EmployeeViewModel = hiltViewModel()
                val aVM: AttendanceViewModel = hiltViewModel()
                AttendanceScreen(
                    employeeViewModel = eVM,
                    attendanceViewModel = aVM
                )
            }

            // 4. Add Employee
            composable(NavRoutes.ADD_EMPLOYEE) {
                val vm: AddEmployeeViewModel = hiltViewModel()
                AddEmployeeScreen(
                    viewModel = vm,
                    onBack = { appNavController.popBackStack() }
                )
            }

            // 5. Employee Detail
            composable(NavRoutes.EMPLOYEE_DETAIL) { backStackEntry ->
                val empId = backStackEntry.arguments?.getString("employeeId")?.toLong() ?: 0L
                val eVM: EmployeeViewModel = hiltViewModel()
                val aVM: AttendanceViewModel = hiltViewModel()
                EmployeeDetailScreen(
                    employeeId = empId,
                    employeeViewModel = eVM,
                    attendanceViewModel = aVM,
                    onBack = { appNavController.popBackStack() }
                )
            }
        }
    }
}