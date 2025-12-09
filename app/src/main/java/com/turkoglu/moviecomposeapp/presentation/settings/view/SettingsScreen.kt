package com.turkoglu.moviecomposeapp.presentation.settings.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
) {
    val currentUser by userViewModel.currentUser.collectAsState()

    // GÖRSEL URL MANTIĞI: Firebase (tam link) veya TMDB (kısa link) kontrolü
    val avatarUrl = currentUser?.avatarUrl
    val fullAvatarUrl = remember(avatarUrl) {
        when {
            avatarUrl.isNullOrEmpty() -> "https://i.pravatar.cc/150?img=12" // Varsayılan resim
            avatarUrl.startsWith("http") -> avatarUrl // Firebase veya harici link
            else -> "https://image.tmdb.org/t/p/w500$avatarUrl" // TMDB linki
        }
    }

    val selectedLang by userViewModel.selectedLanguage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Dialog Durumu
    var showEditDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // --- PROFİL FOTOĞRAFI ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fullAvatarUrl)
                    .placeholder(R.drawable.ic_placeholder) // Drawable klasöründe olduğundan emin ol
                    .error(R.drawable.ic_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 32.dp)
                    .clip(MaterialTheme.shapes.large)
                    // İleride resim değiştirmek istersen buraya tıklama özelliği ekleyebilirsin
                    .clickable { /* Resim seçici açılabilir */ }
            )

            // --- KULLANICI ADI VE DÜZENLEME BUTONU ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = currentUser?.username ?: "Misafir",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Sadece Misafir değilse düzenleme ikonunu göster
                if (currentUser?.isGuest == false) {
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Profili Düzenle",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Text(
                text = "ID: ${currentUser?.id}", // Debug amaçlı veya kullanıcı görsün diye
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- DİL SEÇİMİ ---
            // (LanguageSettingSection fonksiyonunun projende tanımlı olduğunu varsayıyorum)
            LanguageSettingSection(
                selectedLanguage = selectedLang,
                onLanguageSelected = { lang ->
                    coroutineScope.launch {
                        userViewModel.updateLanguage(lang)
                        // Sayfayı yenilemek yerine state değiştiği için UI otomatik güncellenir.
                        // Ancak App genelinde dil değişimi için Activity recreate gerekebilir.
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- HAKKINDA ---
            SettingsItem(
                "Hakkında",
                onClick = { navController.navigate("About") },
                isDestructive = false
            )

            // --- ÇIKIŞ YAP ---
            SettingsItem(
                "Çıkış Yap",
                onClick = {
                    authViewModel.signOut() // Yeni AuthViewModel fonksiyonu
                    navController.navigate("Login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                isDestructive = true
            )
        }

        // --- PROFİL DÜZENLEME DIALOGU ---
        if (showEditDialog && currentUser != null) {
            EditProfileDialog(
                currentName = currentUser!!.username ?: "",
                onDismiss = { showEditDialog = false },
                onConfirm = { newName ->
                    userViewModel.updateUserProfile(currentUser!!.id.toString(), newName)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Profili Düzenle") },
        text = {
            Column {
                Text("Yeni kullanıcı adınızı giriniz:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (text.isNotBlank()) onConfirm(text) }
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        textContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit, isDestructive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f), MaterialTheme.shapes.small) // Hafif arka plan
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

// ... (AboutScreen ve ThemeChoiceChip kodların aynı kalabilir) ...