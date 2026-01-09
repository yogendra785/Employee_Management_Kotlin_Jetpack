package com.example.neutron.data.local.dao

import androidx.room.*
import com.example.neutron.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees ORDER BY name ASC")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM employees WHERE id = :employeeId LIMIT 1")
    fun getEmployeeById(employeeId: Long): Flow<EmployeeEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore if ID already exists
    suspend fun insertEmployee(employee: EmployeeEntity): Long

    @Update
    suspend fun updateEmployee(employee: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM employees WHERE email = :email)")
    suspend fun isEmailExists(email: String): Boolean

    @Query("UPDATE employees SET isActive = :isActive WHERE id = :employeeId")
    suspend fun updateEmployeeStatus(employeeId: Long, isActive: Boolean)
}