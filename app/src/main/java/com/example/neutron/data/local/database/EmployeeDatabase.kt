package com.example.neutron.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.entity.AttendanceEntity
import com.example.neutron.data.local.entity.EmployeeEntity
import com.example.neutron.domain.model.LeaveRequest
import com.example.neutron.data.local.dao.LeaveDao
import java.util.concurrent.Executors

@Database(
    entities = [
        EmployeeEntity::class,
        AttendanceEntity::class,
        LeaveRequest::class
    ],
    version = 7, // 🔹 Incremented to 7 to force a fresh start
    exportSchema = false
)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun leaveDao(): LeaveDao

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

                // 🔹 Manual Injection: This ensures the Admin user is created even if onCreate fails
                Executors.newSingleThreadExecutor().execute {
                    try {
                        instance.openHelper.writableDatabase.execSQL(
                            """
                            INSERT OR IGNORE INTO employees (id, name, email, role, department, isActive, createdAt, password) 
                            VALUES (1, 'Yogendra', 'admin@neutron.com', 'ADMIN', 'IT', 1, ${System.currentTimeMillis()}, '1234')
                            """.trimIndent()
                        )
                        Log.d("NEUTRON_DB", "Admin user verified/inserted successfully")
                    } catch (e: Exception) {
                        Log.e("NEUTRON_DB", "Manual insertion failed: ${e.message}")
                    }
                }

                INSTANCE = instance
                instance
            }
        }
    }
}