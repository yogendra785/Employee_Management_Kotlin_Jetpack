package com.example.neutron.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.entity.AttendanceEntity
import com.example.neutron.data.local.entity.EmployeeEntity

@Database(
    entities = [
        EmployeeEntity::class,
        AttendanceEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getDatabase(context: Context): EmployeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeDatabase::class.java,
                    "employee_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
