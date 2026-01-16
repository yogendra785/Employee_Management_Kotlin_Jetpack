package com.example.neutron.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 🔹 Keep the Enum for use in your UI logic
enum class LeaveStatus { PENDING, APPROVED, REJECTED }

@Entity(tableName = "leave_requests")
data class LeaveRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val employeeId: Long,
    val employeeName: String,
    val startDate: Long,
    val endDate: Long,
    val reason: String,


    val status: String = LeaveStatus.PENDING.name,

    val requestDate: Long = System.currentTimeMillis()
)