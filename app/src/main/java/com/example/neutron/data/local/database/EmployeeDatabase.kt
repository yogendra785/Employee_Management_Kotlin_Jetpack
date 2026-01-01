package com.example.neutron.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.entity.EmployeeEntity

@Database(
    entities = [EmployeeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NeutronDatabase: RoomDatabase(){
    abstract fun employeeDao(): EmployeeDao
}