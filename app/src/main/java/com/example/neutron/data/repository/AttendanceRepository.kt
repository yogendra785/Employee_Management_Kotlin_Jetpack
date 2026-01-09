package com.example.neutron.data.repository

import com.example.neutron.data.local.dao.AttendanceDao
import com.example.neutron.data.mapper.toAttendance
import com.example.neutron.data.mapper.toAttendanceEntity
import com.example.neutron.domain.model.Attendance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttendanceRepository(
    private val attendanceDao: AttendanceDao
) {
    fun getAttendanceByDate(date: Long): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceByDate(date)
            .map { entities ->
                entities.map { it.toAttendance() }
            }
    }

    fun getAttendanceForEmployee(employeeId: Long): Flow<List<Attendance>> {
        return attendanceDao.getAttendanceForEmployee(employeeId)
            .map { entities ->
                entities.map { it.toAttendance() }
            }
    }

    // Helpful for a general dashboard or full history view
    fun getAllAttendance(): Flow<List<Attendance>> {
        return attendanceDao.getAllAttendance()
            .map { entities ->
                entities.map { it.toAttendance() }
            }
    }

    suspend fun markAttendance(attendance: Attendance) {
        attendanceDao.upsertAttendance(attendance.toAttendanceEntity())
    }
}