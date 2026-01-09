package com.example.neutron.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neutron.data.local.database.EmployeeDatabase
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.data.repository.EmployeeRepository
import com.example.neutron.screens.attendance.AttendanceScreen
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employee.*
import com.example.neutron.viewmodel.attendance.*
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.employee.*

@Composable
fun MainScaffold(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {
    // 🔹 This internal controller manages Dashboard, Employee, and Attendance tabs
    val appNavController = rememberNavController()

    val context = LocalContext.current
    val database = remember { EmployeeDatabase.getDatabase(context) }
    val empRepo = remember { EmployeeRepository(database.employeeDao()) }
    val attRepo = remember { AttendanceRepository(database.attendanceDao()) }

    Scaffold(
        bottomBar = {
            // 🔹 BottomBar uses the internal appNavController
            BottomBar(navController = appNavController)
        }
    ) { paddingValues ->
        NavHost(
            navController = appNavController,
            startDestination = NavRoutes.DASHBOARD,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.DASHBOARD) {
                DashboardScreen(navController = appNavController, authViewModel = authViewModel)
            }

            composable(NavRoutes.EMPLOYEE) {
                val vm: EmployeeViewModel = viewModel(factory = EmployeeViewModelFactory(empRepo))
                EmployeeListScreen(viewModel = vm, navigate = { appNavController.navigate(it) })
            }

            composable(NavRoutes.ATTENDANCE) {
                val eVM: EmployeeViewModel = viewModel(factory = EmployeeViewModelFactory(empRepo))
                val aVM: AttendanceViewModel = viewModel(factory = AttendanceViewModelFactory(attRepo))
                AttendanceScreen(employeeViewModel = eVM, attendanceViewModel = aVM)
            }

            composable(NavRoutes.ADD_EMPLOYEE) {
                val vm: AddEmployeeViewModel = viewModel(factory = AddEmployeeViewModelFactory(empRepo))
                AddEmployeeScreen(viewModel = vm, onBack = { appNavController.popBackStack() })
            }

            // Detail screen added back to the internal graph
            composable(NavRoutes.EMPLOYEE_DETAIL) { backStackEntry ->
                val empId = backStackEntry.arguments?.getString("employeeId")?.toLong() ?: 0L
                val eVM: EmployeeViewModel = viewModel(factory = EmployeeViewModelFactory(empRepo))
                val aVM: AttendanceViewModel = viewModel(factory = AttendanceViewModelFactory(attRepo))
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