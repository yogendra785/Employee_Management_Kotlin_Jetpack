package com.example.neutron.domain.model

enum class AttendanceStatus {
    PRESENT,
    ABSENT
}

data class Attendance(
    val id: Long = 0, // Default 0 for new records
    val employeeId: Long,
    val date: Long,
    val status: AttendanceStatus
)