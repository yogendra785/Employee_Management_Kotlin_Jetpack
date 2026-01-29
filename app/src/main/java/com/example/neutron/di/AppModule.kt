package com.example.neutron.di

import android.content.Context
import androidx.room.Room
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.dao.LeaveDao
import com.example.neutron.data.local.dao.SalaryDao // 🔹 Added
import com.example.neutron.data.local.database.EmployeeDatabase
import com.example.neutron.data.repository.AttendanceRepository
import com.example.neutron.data.repository.EmployeeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- Database Providers ---

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EmployeeDatabase {
        return EmployeeDatabase.getDatabase(context) // 🔹 Use the companion object method you defined
    }

    @Provides
    fun provideLeaveDao(database: EmployeeDatabase): LeaveDao = database.leaveDao()

    @Provides
    fun provideEmployeeDao(db: EmployeeDatabase): EmployeeDao = db.employeeDao()

    @Provides
    fun provideAttendanceDao(db: EmployeeDatabase): AttendanceDao = db.attendanceDao()

    @Provides
    fun provideSalaryDao(db: EmployeeDatabase): SalaryDao = db.salaryDao() // 🔹 Added SalaryDao provider

    // --- Repository Providers ---

    @Provides
    @Singleton
    fun provideAttendanceRepository(dao: AttendanceDao) = AttendanceRepository(dao)

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        dao: EmployeeDao,
        salaryDao: SalaryDao, // 🔹 Added this parameter
        @ApplicationContext context: Context
    ): EmployeeRepository {
        return EmployeeRepository(dao, salaryDao, context) // 🔹 Now matches your updated constructor
    }
}