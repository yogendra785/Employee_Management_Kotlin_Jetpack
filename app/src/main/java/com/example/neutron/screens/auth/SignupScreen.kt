package com.example.neutron.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.neutron.viewmodel.auth.AuthState
import com.example.neutron.viewmodel.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onLoginClick: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Sign Up") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    authViewModel.signup(email, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }

            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account?")
            }

            when (authState) {
                is AuthState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthState.Error -> {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> Unit
            }
        }
    }
}
