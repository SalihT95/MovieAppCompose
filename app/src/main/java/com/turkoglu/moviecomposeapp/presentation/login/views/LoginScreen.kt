package com.turkoglu.moviecomposeapp.presentation.login.views

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.login.LoginUiState
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    var username by rememberSaveable { mutableStateOf(viewModel.savedUsername) }
    var password by rememberSaveable { mutableStateOf(viewModel.savedPassword) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(viewModel.rememberMe) }
    val loginState = viewModel.loginState

    LaunchedEffect(Unit) {
        if (rememberMe && username.isNotBlank() && password.isNotBlank()) {
            viewModel.login(username, password)
        }
    }

    val signupIntent = Intent(Intent.ACTION_VIEW, "https://www.themoviedb.org/signup".toUri())
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.the_movie_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Kullanıcı Adı") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        rememberMe = !rememberMe
                        viewModel.updateRememberMe(rememberMe)
                    }
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = {
                        rememberMe = it
                        viewModel.updateRememberMe(it)
                    },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Beni Hatırla", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = {
                    viewModel.login(username.trim(), password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = username.isNotBlank() && password.isNotBlank() && loginState !is LoginUiState.Loading
            ) {
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Giriş Yap", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hesabın yok mu? Kayıt ol",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable {
                    launcher.launch(signupIntent)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (loginState) {
                is LoginUiState.Error -> {
                    Text(
                        text = "Hata: ${loginState.message}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                is LoginUiState.Success -> {
                    LaunchedEffect(Unit) {
                        userViewModel.setAccount(loginState.account)
                        userViewModel.saveUser(loginState.account)
                        println("Login başarılı: Account: ${loginState.account}")
                        onLoginSuccess()
                    }

                    Text(
                        text = "Hoş Geldin ${loginState.account.username}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                else -> {}
            }
        }
    }
}
