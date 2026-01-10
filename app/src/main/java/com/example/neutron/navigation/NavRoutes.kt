package com.example.neutron.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard_screen"
    const val EMPLOYEE = "employee_screen"
    const val ATTENDANCE = "attendance"
    const val ADD_EMPLOYEE = "add_employee"

    // Route with argument
    const val EMPLOYEE_DETAIL = "employee_detail/{employeeId}"
    fun createDetailRoute(id: Long) = "employee_detail/$id"
}