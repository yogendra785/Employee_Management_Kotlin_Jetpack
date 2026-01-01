package com.example.neutron.screens.attendance


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AttendanceScreen() {
    Column(
        modifier= Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Text("Attendance Screen")
    }

}
