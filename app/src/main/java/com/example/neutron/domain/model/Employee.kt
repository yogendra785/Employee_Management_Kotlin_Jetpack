package com.example.neutron.domain.model

data class Employee(
    val id: Long = 0,
    val name: String,
    val email: String,
    val role: String,
    val department: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val password: String // Added password field
)