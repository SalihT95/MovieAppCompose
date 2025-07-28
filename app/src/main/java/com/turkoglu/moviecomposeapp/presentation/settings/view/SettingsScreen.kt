package com.turkoglu.moviecomposeapp.presentation.settings.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.login.AuthViewModel
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel
import androidx.core.net.toUri

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val avatarUrl = currentUser?.avatarUrl
    val fullAvatarUrl = avatarUrl?.let { path ->
        if (path.startsWith("/")) "https://image.tmdb.org/t/p/w500$path" else null
    }
    val selectedLang by userViewModel.selectedLanguage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.backend),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fullAvatarUrl ?: "https://i.pravatar.cc/150?img=3")
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 32.dp)
                    .clip(MaterialTheme.shapes.large)
            )

            Text(
                text = currentUser?.username ?: "Guest",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.padding(24.dp))

            LanguageSettingSection(
                selectedLanguage = selectedLang,
                onLanguageSelected = {
                    userViewModel.updateLanguage(it)
                    navController.navigate("Settings") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(24.dp))

            SettingsItem("About", onClick = {
                navController.navigate("About")
            }, isDestructive = false)


            SettingsItem("Log Out", isDestructive = true, onClick = {
                viewModel.clearCredentials()
                navController.navigate("Login") {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit, isDestructive: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hakkında") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                }
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Bu uygulamada kullanılan tüm film ve dizi verileri The Movie Database (TMDB) API aracılığıyla sağlanmaktadır.")
            Text(
                text = "https://www.themoviedb.org/",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://www.themoviedb.org".toUri()
                        )
                    )
                }
            )
            Text(
                text = "Bu ürün TMDB API'sini kullanmaktadır ancak TMDB tarafından onaylanmamıştır veya desteklenmemektedir.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
