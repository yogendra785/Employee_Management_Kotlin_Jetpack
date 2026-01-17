package com.example.neutron.screens.leave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.leave.LeaveViewModel

@Composable
fun MyLeaveHistoryScreen(
    viewModel: LeaveViewModel,
    onBack: ()->Unit
){
    //initially  sing 123 as placeholder for logged in ID
    val myLeaves by viewModel.getMyLeaves(123).collectAsState(initial = emptyList())

    Column(
        modifier=Modifier.fillMaxSize().padding(16.dp)
    ){
        Row(verticalAlignment = Alignment.CenterVertically){
            IconButton(onClick = onBack) {Icon(Icons.AutoMirrored.Filled.ArrowBack,null)}
            Text("My Leave Status",style = MaterialTheme.typography.headlineSmall)
        }
        LazyColumn {
            items(myLeaves) { request ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ){
                    Column (modifier = Modifier.padding(16.dp)){
                        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                            Text(formatDate(request.startDate),style = MaterialTheme.typography.titleMedium)

                            //status badge
                            val badgeColor = when(request.status){
                                "APPROVED" ->Color(0xFF4CAF50)
                                "REJECTED" -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.primary
                            }
                            Surface(color = badgeColor,  shape = RoundedCornerShape(4.dp)){
                                Text(
                                    text = request.status,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        Text("Reason:${request.reason}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}