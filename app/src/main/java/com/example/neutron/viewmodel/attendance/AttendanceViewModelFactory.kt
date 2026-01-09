package com.example.neutron.viewmodel.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neutron.data.repository.AttendanceRepository

class AttendanceViewModelFactory(
    private val repository: AttendanceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            AttendanceViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}