package com.example.neutron.screens.employee

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun EmptyEmployeeState(){
    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text="No employee found",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}