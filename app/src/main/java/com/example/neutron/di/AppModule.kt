package com.example.neutron.di

import android.content.Context
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.dao.LeaveDao
import com.example.neutron.data.local.database.EmployeeDatabase
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.data.repository.EmployeeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EmployeeDatabase {
        return EmployeeDatabase.getDatabase(context)
    }

    @Provides
    fun provideLeaveDao(database: EmployeeDatabase): LeaveDao {
        return database.leaveDao()
    }

    @Provides
    fun provideEmployeeDao(db: EmployeeDatabase): EmployeeDao = db.employeeDao()

    @Provides
    fun provideAttendanceDao(db: EmployeeDatabase): AttendanceDao = db.attendanceDao()

    // 🔹 These providers tell Hilt how to build your repositories
    @Provides
    @Singleton
    fun provideEmployeeRepository(dao: EmployeeDao) = EmployeeRepository(dao)

    @Provides
    @Singleton
    fun provideAttendanceRepository(dao: AttendanceDao) = AttendanceRepository(dao)
}