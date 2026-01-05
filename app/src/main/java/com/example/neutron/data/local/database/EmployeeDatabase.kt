package com.example.neutron.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.entity.EmployeeEntity

@Database(
    entities = [EmployeeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getDatabase(context: Context): EmployeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeDatabase::class.java,
                    "employee_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
