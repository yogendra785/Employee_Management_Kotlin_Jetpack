package com.example.neutron.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.screens.attendance.AttendanceScreen
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employee.AddEmployeeScreen
import com.example.neutron.screens.employee.EmployeeDetailScreen
import com.example.neutron.screens.employee.EmployeeListScreen
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import com.example.neutron.viewmodel.employee.EmployeeViewModelFactory

@Composable
fun NeutronNavHost(
    navController: NavHostController,
    employeeRepository: EmployeeRepository
) {

    val employeeViewModel: EmployeeViewModel = viewModel(
        factory = EmployeeViewModelFactory(employeeRepository)
    )

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = NavRoutes.DASHBOARD,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(NavRoutes.DASHBOARD) {
                DashboardScreen(navController)
            }

            composable(NavRoutes.EMPLOYEE) {
                EmployeeListScreen(
                    viewModel = employeeViewModel,
                    navigate = { route ->
                        navController.navigate(route)
                    }
                )
            }


            composable("add_employee") {
                AddEmployeeScreen(
                    onEmployeeSaved = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "employee_detail/{employeeId}",
                arguments = listOf(
                    navArgument("employeeId") { type = NavType.LongType }
                )
            ) { backStackEntry ->

                val employeeId =
                    backStackEntry.arguments?.getLong("employeeId")
                        ?: return@composable

                EmployeeDetailScreen(
                    employeeId = employeeId,
                    viewModel = employeeViewModel
                )
            }

            composable(NavRoutes.ATTENDANCE) {
                AttendanceScreen()
            }
        }
    }
}
