package com.taskmanager.activity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskmanager.activity.viewmodel.CreateAccountViewModel
import com.taskmanager.base.Constants

@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.pass.collectAsState()
    val msgErro by viewModel.msgError.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Constants.AUTHENTICATION.REGISTER,
                fontSize = 32.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            OutlinedTextField(
                value = email, onValueChange = { viewModel.setEmail(it) },
                label = { Text(text = Constants.AUTHENTICATION.EMAIL) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )
            OutlinedTextField(
                value = password, onValueChange = { viewModel.setPassword(it) },
                label = { Text(text = Constants.AUTHENTICATION.PASSWORD) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            if (msgErro.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 10.dp), contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = msgErro,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 18.sp,
                        color = Color.Red
                    )
                }
            }
            Button(
                onClick = { viewModel.saveUser(email, password) }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = Constants.AUTHENTICATION.REGISTER)
            }
        }
    }
}