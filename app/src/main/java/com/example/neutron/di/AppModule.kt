package com.example.neutron.di

import android.content.Context
import androidx.room.Room
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.dao.LeaveDao
import com.example.neutron.data.local.database.EmployeeDatabase
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.data.repository.EmployeeRepository
import com.google.firebase.auth.FirebaseAuth // 🔹 Added
import com.google.firebase.firestore.FirebaseFirestore // 🔹 Added for Role management
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Firebase Providers ---

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance() // 🔹 Provides Firebase Auth

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance() // 🔹 Provides Firestore

    // --- Database Providers ---

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EmployeeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EmployeeDatabase::class.java,
            "employee_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideLeaveDao(database: EmployeeDatabase): LeaveDao {
        return database.leaveDao()
    }

    @Provides
    fun provideEmployeeDao(db: EmployeeDatabase): EmployeeDao = db.employeeDao()

    @Provides
    fun provideAttendanceDao(db: EmployeeDatabase): AttendanceDao = db.attendanceDao()

    // --- Repository Providers ---

    @Provides
    @Singleton
    fun provideEmployeeRepository(dao: EmployeeDao) = EmployeeRepository(dao)

    @Provides
    @Singleton
    fun provideAttendanceRepository(dao: AttendanceDao) = AttendanceRepository(dao)
}