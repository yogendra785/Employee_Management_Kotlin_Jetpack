package com.example.neutron.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neutron.screens.attendance.AttendanceScreen
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employee.*
import com.example.neutron.screens.leave.AdminLeaveListScreen
import com.example.neutron.screens.leaverequestscreen.LeaveRequestScreen
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.employee.*
import com.example.neutron.viewmodel.leave.LeaveViewModel

@Composable
fun MainScaffold(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {

    val appNavController = rememberNavController()

    Scaffold(
        bottomBar = {

            BottomBar(navController = appNavController)
        }
    ) { paddingValues ->

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

            //6. Leave Request
            composable(NavRoutes.LEAVE_REQUEST){
                val leaveVM: LeaveViewModel = hiltViewModel()
                LeaveRequestScreen(
                    viewModel =  leaveVM,
                    onBack = {appNavController.popBackStack()}
                )
            }
            //7-leave request admin screen
            composable(NavRoutes.ADMIN_LEAVE_LIST){
                val leaveVM: LeaveViewModel = hiltViewModel()
                AdminLeaveListScreen(
                    viewModel  = leaveVM,
                    onBack = {appNavController.popBackStack()}
                )
            }
        }
    }
}