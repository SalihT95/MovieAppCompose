package com.turkoglu.moviecomposeapp.presentation.login.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.login.LoginUiState
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit // Web yerine uygulama içi kayıt ekranına gitmek için
) {
    // Odak yöneticisi (Klavye kapatmak için)
    val focusManager = LocalFocusManager.current

    // Firebase için Username yerine Email kullanıyoruz
    var email by rememberSaveable { mutableStateOf(viewModel.savedEmail) }
    var password by rememberSaveable { mutableStateOf(viewModel.savedPassword) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Beni Hatırla Durumu
    val rememberMe by viewModel.rememberMe.collectAsStateWithLifecycle()

    // Ekran Durumu (Loading, Success, Error)
    val loginState = viewModel.loginState

    // --- OTOMATİK GİRİŞ KONTROLÜ ---
    LaunchedEffect(Unit) {
        // Eğer beni hatırla açık ve bilgiler kayıtlıysa otomatik dene
        if (rememberMe && email.isNotBlank() && password.isNotBlank()) {
            viewModel.login(email, password)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.the_movie_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- KULLANICI ADI VEYA EMAIL ALANI ---
            OutlinedTextField(
                value = email, // Değişken adını 'identifier' veya 'loginInput' yapabilirsin ama 'email' kalsa da çalışır
                onValueChange = { email = it },
                label = { Text("Kullanıcı Adı veya E-posta") }, // DEĞİŞTİ
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, // İkonu Person yaptık
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, // DEĞİŞTİ: Email yerine Text
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- ŞİFRE ALANI ---
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Şifre") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(id = android.R.drawable.ic_menu_view) // Veya kendi ikonun
                    else
                        painterResource(id = android.R.drawable.ic_secure) // Veya kendi ikonun

                    // İkon yoksa Icon vector kullanabilirsin, aşağıda Text ile örnekledim:
                    val iconText = if (passwordVisible) "Gizle" else "Göster"
                    Text(
                        text = iconText,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { passwordVisible = !passwordVisible },
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done // Klavyede "Tamam" butonu
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus() // Klavyeyi kapat
                        if (email.isNotBlank() && password.isNotBlank() && loginState !is LoginUiState.Loading) {
                            viewModel.login(email.trim(), password)
                        }
                    }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- BENİ HATIRLA ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.updateRememberMe(!rememberMe) }
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { checked -> viewModel.updateRememberMe(checked) },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Beni Hatırla", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- GİRİŞ YAP BUTONU ---
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.login(email.trim(), password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = email.isNotBlank() && password.isNotBlank() && loginState !is LoginUiState.Loading
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

            // --- KAYIT OL LINK ---
            Text(
                text = "Hesabın yok mu? Kayıt ol",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable {
                    onNavigateToRegister() // Artık uygulama içi ekrana gidiyor
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- MİSAFİR GİRİŞİ ---
            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.loginAsGuest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Üye Olmadan Devam Et", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- DURUM YÖNETİMİ (ERROR / SUCCESS) ---
            when (loginState) {
                is LoginUiState.Error -> {
                    Text(
                        text = "Hata: ${loginState.message}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                is LoginUiState.Success -> {
                    // Yan Etki: Giriş başarılı olduğunda çalışır
                    LaunchedEffect(loginState.userId) {
                        // UserViewModel'e global kullanıcıyı set et
                        userViewModel.setAccount(loginState.account)
                        userViewModel.saveUser(loginState.account) // Opsiyonel: Room'a kaydet

                        // Ana ekrana yönlendir
                        onLoginSuccess()
                    }

                    // UI'da kısa bir bilgi gösterebilirsin
                    Text(
                        text = "Giriş Başarılı, Yönlendiriliyorsunuz...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                else -> Unit
            }
        }
    }
}