package com.turkoglu.moviecomposeapp.presentation.settings.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items // BU IMPORT EKLENDİ (Grid hatasını çözer)
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.turkoglu.moviecomposeapp.presentation.component.ProfileImage
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel
import com.turkoglu.moviecomposeapp.util.AvatarUtils

@Composable
fun SettingsScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val selectedLang by userViewModel.selectedLanguage.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Dialog Durumları
    var showEditDialog by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(32.dp))

            // --- PROFİL FOTOĞRAFI ---
            Box(contentAlignment = Alignment.BottomEnd) {
                ProfileImage(
                    avatarKey = currentUser?.avatarUrl,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable {
                            // Sadece misafir değilse avatar dialog açılır
                            if (currentUser?.isGuest == false) {
                                showAvatarDialog = true
                            }
                        }
                )

                // Düzenleme İkonu (Sadece üyelere)
                if (currentUser?.isGuest == false) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Avatar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(28.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(6.dp)
                    )
                }
            }

            if (currentUser?.isGuest == false) {
                Text(
                    text = "Avatarı değiştirmek için dokun",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

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

                // Eğer kullanıcı misafir değilse düzenleme butonu görünür
                if (currentUser != null && !currentUser!!.isGuest) {
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

            Spacer(modifier = Modifier.height(24.dp))

            // Dil Ayarları (LanguageSettingSection kodunu paylaşmadığın için buraya import edilmiş varsayıyorum)
            /* LanguageSettingSection(
                selectedLanguage = selectedLang,
                onLanguageSelected = { lang ->
                    coroutineScope.launch { userViewModel.updateLanguage(lang) }
                }
            )
            */

            Spacer(modifier = Modifier.height(16.dp))

            SettingsItem(
                "Hakkında",
                onClick = { navController.navigate("About") },
                isDestructive = false
            )
            SettingsItem("Çıkış Yap", onClick = {
                userViewModel.logout() // ViewModel'deki logout user'ı null yapar
                authViewModel.signOut()
                navController.navigate("Login") { popUpTo(0) { inclusive = true } }
            }, isDestructive = true)

        } // Column Sonu

        // --- DIALOG ÇAĞRILARI ---

        // 1. Profil Adı Düzenleme Dialogu
        if (showEditDialog && currentUser != null) {
            EditProfileDialog(
                currentName = currentUser!!.username ?: "",
                onDismiss = { showEditDialog = false },
                onConfirm = { newName ->
                    // id null değilse güncelle
                    currentUser?.id?.let { uid ->
                        userViewModel.updateUserProfile(uid, newName)
                    }
                    showEditDialog = false
                }
            )
        }

        // 2. Avatar Seçim Dialogu
        if (showAvatarDialog) {
            AvatarSelectionDialog(
                currentAvatarKey = currentUser?.avatarUrl,
                onDismiss = { showAvatarDialog = false },
                onAvatarSelected = { selectedKey ->
                    userViewModel.updateAvatar(selectedKey)
                    showAvatarDialog = false
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
    // DÜZELTME: currentName değiştiğinde TextField da güncellensin diye 'remember(currentName)' kullanıldı.
    var text by remember(currentName) { mutableStateOf(currentName) }

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
fun AvatarSelectionDialog(
    currentAvatarKey: String?,
    onDismiss: () -> Unit,
    onAvatarSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Avatar Seç", textAlign = TextAlign.Center) },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(300.dp)
            ) {
                // items importu yukarıda eklendiği için artık hata vermez
                items(AvatarUtils.selectableAvatars) { key ->
                    val resId = AvatarUtils.getDrawableId(key)
                    val isSelected = currentAvatarKey == key

                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onAvatarSelected(key) }
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("İptal") } },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit, isDestructive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                MaterialTheme.shapes.small
            )
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