package com.example.neutron.data.local.dao

import androidx.room.*
import com.example.neutron.data.local.entity.EmployeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employees ORDER BY createdAt DESC")
    fun getALLEmplpoyees(): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)

    @Query("UPDATE employees SET isActive = :active WHERE id = :employeeID")
    suspend fun updateEmployeeStatus(
        employeeID: Long,
        active: Boolean
    )
}


