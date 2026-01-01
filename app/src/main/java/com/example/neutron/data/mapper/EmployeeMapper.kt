package com.example.neutron.data.mapper

import com.example.neutron.data.local.entity.EmployeeEntity
import com.example.neutron.model.Employee

fun EmployeeEntity.toEmployee():Employee{
    return Employee(
        id = id,
        name = name,
        email = email,
        role = role,
        department = department,
        isActive = isActive
    )
}