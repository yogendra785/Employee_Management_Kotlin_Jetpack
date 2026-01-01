package com.example.neutron.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey
    val id: Long,
    val name:String,
    val email: String,
    val role: String,
    val department: String, //for rolebased -Admin/Employee
    val isActive: Boolean=true,
    val createdAt: Long
)