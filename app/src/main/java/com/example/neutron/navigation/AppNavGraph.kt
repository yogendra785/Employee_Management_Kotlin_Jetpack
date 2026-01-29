package com.example.neutron.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.neutron.screens.attendance.AttendanceScreen
import com.example.neutron.screens.dashboard.DashboardScreen
import com.example.neutron.screens.employee.AddEmployeeScreen
import com.example.neutron.screens.employee.EmployeeDetailScreen
import com.example.neutron.screens.employee.EmployeeListScreen
import com.example.neutron.screens.leave.AdminLeaveListScreen
import com.example.neutron.screens.leave.LeaveRequestScreen
import com.example.neutron.screens.leave.MyLeaveHistoryScreen
import com.example.neutron.screens.profile.ProfileScreen
import com.example.neutron.screens.salary.SalaryManagementScreen
import com.example.neutron.viewmodel.attendance.AttendanceViewModel
import com.example.neutron.viewmodel.auth.AuthViewModel
import com.example.neutron.viewmodel.employee.AddEmployeeViewModel
import com.example.neutron.viewmodel.employee.EmployeeViewModel
import com.example.neutron.viewmodel.leave.LeaveViewModel

fun NavGraphBuilder.appNavGraph(
    appNavController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userRole: String
) {
    composable(NavRoutes.DASHBOARD) {
        DashboardScreen(
            navController = appNavController,
            authViewModel = authViewModel
        )
    }
    composable(NavRoutes.SALARY_MANAGEMENT) {
        SalaryManagementScreen(
            onBackClick = { appNavController.popBackStack() }
        )
    }
    if (userRole == "ADMIN") {
        adminNavGraph(appNavController)
    }
    employeeNavGraph(appNavController, userRole)
    commonNavGraph(appNavController, rootNavController, authViewModel)
}

private fun NavGraphBuilder.adminNavGraph(navController: NavHostController) {

    composable(NavRoutes.ADD_EMPLOYEE) {
        val vm: AddEmployeeViewModel = hiltViewModel()
        AddEmployeeScreen(
            viewModel = vm,
            onBack = { navController.popBackStack() })
    }
    composable(NavRoutes.ADMIN_LEAVE_LIST) {
        val leaveVM: LeaveViewModel = hiltViewModel()
        AdminLeaveListScreen(
            viewModel = leaveVM,
            onBack = { navController.popBackStack() })
    }
}

private fun NavGraphBuilder.employeeNavGraph(navController: NavHostController, userRole: String) {
    if (userRole == "ADMIN") {
        composable(NavRoutes.EMPLOYEE) {
            val vm: EmployeeViewModel = hiltViewModel()
            EmployeeListScreen(
                viewModel = vm,
                navigate = { route -> navController.navigate(route) })
        }
    }
    composable(NavRoutes.ATTENDANCE) {
        val eVM: EmployeeViewModel = hiltViewModel()
        val aVM: AttendanceViewModel = hiltViewModel()
        AttendanceScreen(
            employeeViewModel = eVM,
            attendanceViewModel = aVM,
            userRole = userRole
        )
    }
    composable(NavRoutes.LEAVE_REQUEST) {
        val leaveVM: LeaveViewModel = hiltViewModel()
        LeaveRequestScreen(
            viewModel = leaveVM,
            onBack = { navController.popBackStack() })
    }
    composable(NavRoutes.MY_LEAVE_HISTORY) {
        val leaveVM: LeaveViewModel = hiltViewModel()
        MyLeaveHistoryScreen(
            viewModel = leaveVM,
            onBack = { navController.popBackStack() })
    }
}

private fun NavGraphBuilder.commonNavGraph(
    appNavController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel
) {
    composable(NavRoutes.EMPLOYEE_DETAIL) { backStackEntry ->
        val empId = backStackEntry.arguments?.getString("employeeId")?.toLong() ?: 0L
        val eVM: EmployeeViewModel = hiltViewModel()
        val aVM: AttendanceViewModel = hiltViewModel()
        EmployeeDetailScreen(
            employeeId = empId,
            employeeViewModel = eVM,
            attendanceViewModel = aVM,
            onBack = { appNavController.popBackStack() })
    }
    composable(NavRoutes.PROFILE) {
        ProfileScreen(
            navController = appNavController,
            rootNavController = rootNavController,
            authViewModel = authViewModel,
            onLogout = {
                authViewModel.logout()
                rootNavController.navigate(NavRoutes.LOGIN) {
                    popUpTo(0)
                }
            }
        )
    }
}
