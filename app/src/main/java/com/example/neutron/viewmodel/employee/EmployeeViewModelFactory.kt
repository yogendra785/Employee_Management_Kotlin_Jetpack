package com.example.neutron.viewmodel.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.neutron.data.repository.EmployeeRepository

class EmployeeViewModelFactory(
    private val repository: EmployeeRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EmployeeViewModel::class.java) -> {
                EmployeeViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}