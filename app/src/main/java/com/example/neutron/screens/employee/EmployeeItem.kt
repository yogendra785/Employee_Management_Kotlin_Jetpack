package com.example.neutron.screens.employee

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.neutron.domain.model.Employee


@Composable
fun EmployeeItem(
    employee: Employee,
    onToggleActive: () -> Unit,
    onClick: () -> Unit

){
    Card(
        modifier=Modifier.fillMaxWidth(),
        elevation= CardDefaults.cardElevation(4.dp)
    ){
        Row(
            modifier= Modifier
                .clickable{onToggleActive()}
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier=Modifier.weight(1f)
            ){
                Text(
                    text=employee.name,
                    style=MaterialTheme.typography.titleMedium

                )

                Text(
                    text=employee.department,
                    style=MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text= if(employee.isActive) "Active" else "Inactive",
                color = if(employee.isActive)
                MaterialTheme.colorScheme.primary
                else
                MaterialTheme.colorScheme.error,
                textDecoration =
                    if (!employee.isActive) TextDecoration.LineThrough
                else TextDecoration.None
            )
        }

    }
}