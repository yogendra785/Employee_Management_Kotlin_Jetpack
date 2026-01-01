package com.example.neutron.model

data class Employee(
    val id:Long,
    val name: String,
    val email: String,
    val role:String,
    val department:String,
    val isActive: Boolean
)