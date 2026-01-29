package com.example.neutron.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.local.dao.EmployeeDao
import com.example.neutron.data.local.dao.SalaryDao // Import this
import com.example.neutron.data.local.entity.AttendanceEntity
import com.example.neutron.data.local.entity.SalaryEntity
import com.example.neutron.data.mapper.toEmployee
import com.example.neutron.data.mapper.toEmployeeEntity
import com.example.neutron.domain.model.AttendanceStatus
import com.example.neutron.domain.model.Employee
import com.example.neutron.domain.model.SalaryRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EmployeeRepository(
    private val dao: EmployeeDao,
    private val salaryDao: SalaryDao,

    private val attendanceDao: AttendanceDao,
    private val context: Context,
) {

    // --- Employee Functions ---

    fun getAllEmployees(): Flow<List<Employee>> {
        return dao.getAllEmployees().map { entities ->
            entities.map { it.toEmployee() }
        }
    }

    fun getEmployeeById(id: Long): Flow<Employee?> {
        return dao.getEmployeeById(id).map { it?.toEmployee() }
    }

    suspend fun insertEmployeeWithImage(employee: Employee, imageUri: Uri?) {
        val finalPath = imageUri?.let { saveImageToInternalStorage(it) }
        val employeeWithImage = finalPath?.let { employee.copy(imagePath = it) } ?: employee
        dao.insertEmployee(employeeWithImage.toEmployeeEntity())
    }

    private suspend fun saveImageToInternalStorage(uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error saving image: ${e.message}")
            null
        }
    }

    suspend fun updateEmployee(employee: Employee) {
        dao.updateEmployee(employee.toEmployeeEntity())
    }

    suspend fun deleteEmployee(employee: Employee) {
        dao.deleteEmployee(employee.toEmployeeEntity())
    }

    suspend fun updateEmployeeStatus(id: Long, isActive: Boolean) {
        dao.updateEmployeeStatus(id, isActive)
    }

    suspend fun isEmailExists(email: String): Boolean {
        return dao.isEmailExists(email)
    }

    suspend fun findEmployeeById(id: Long): Employee? {
        return try {
            dao.getEmployeeById(id).map { it?.toEmployee() }.firstOrNull()
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error finding employee by ID: $id", e)
            null
        }
    }

    // --- Salary Functions ---

    suspend fun addSalaryRecord(salary: SalaryRecord) = withContext(Dispatchers.IO) {
        val entity = SalaryEntity(
            employeeId = salary.employeeId,
            month = salary.month,
            baseSalary = salary.baseSalary,
            advancePaid = salary.advancePaid,
            absentDays = salary.absentDays,
            perDayDeduction = salary.perDayDeduction
        )
        // 2. Used salaryDao instead of dao
        salaryDao.insertSalary(entity)
    }
    // 1. Logic to count absences for a specific month
    // 1. Logic to count absences for a specific month
    suspend fun getAbsentCount(employeeId: Long, month: String): Int = withContext(Dispatchers.IO) {
        try {
            // 🔹 FIX: Changed 'dao' to 'attendanceDao'
            val list: List<AttendanceEntity> = attendanceDao.getAttendanceByEmployee(employeeId).firstOrNull() ?: emptyList()

            list.count { attendance: AttendanceEntity ->
                // Use .name if status is an Enum in your Entity, or just the string comparison
                attendance.status == AttendanceStatus.ABSENT.name && isDateInMonth(attendance.date, month)
            }
        } catch (e: Exception) {
            Log.e("EmployeeRepository", "Error counting absences", e)
            0
        }
    }

    private fun isDateInMonth(timestamp: Long, monthYearString: String): Boolean {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        // SimpleDateFormat fix
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val formattedDate = sdf.format(cal.time)
        return formattedDate.equals(monthYearString, ignoreCase = true)
    }

    // In EmployeeRepository.kt
    fun calculateNetSalary(base: Double, advance: Double, absentDays: Int, perDay: Double): Double {
        val totalDeduction = absentDays * perDay
        return base - totalDeduction - advance
    }
}