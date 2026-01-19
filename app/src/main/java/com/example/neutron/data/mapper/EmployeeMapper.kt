package com.example.neutron.data.mapper

import com.example.neutron.data.local.entity.EmployeeEntity
import com.example.neutron.domain.model.Employee

// Entity → Domain model
fun EmployeeEntity.toEmployee(): Employee {
    return Employee(
        id = id,
        name = name,
        email = email,
        role = role,
        department = department,
        isActive = isActive,
        createdAt = createdAt,
        password = password
    )
}

// Domain model → Entity
fun Employee.toEmployeeEntity(): EmployeeEntity {
    return EmployeeEntity(
        id = id,
        name = name,
        email = email,
        role = role,
        department = department,
        isActive = isActive,
        createdAt = createdAt,
        password = password
    )
}