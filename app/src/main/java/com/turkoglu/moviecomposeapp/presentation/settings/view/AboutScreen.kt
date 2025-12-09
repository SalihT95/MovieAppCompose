package com.turkoglu.moviecomposeapp.presentation.settings.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.BuildConfig
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hakkında", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackgroundGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- APP INFO ---
                AppInfoSection()

                Spacer(modifier = Modifier.height(24.dp))

                // --- DEVELOPER INFO (Web Sitesi Eklendi) ---
                DeveloperInfoSection(context)

                Spacer(modifier = Modifier.height(24.dp))

                // --- TMDB DISCLAIMER ---
                TmdbAttributionSection(context)

                Spacer(modifier = Modifier.height(24.dp))

                // --- TECH STACK ---
                TechStackSection()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AppInfoSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.the_movie_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Movie App",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Version ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DeveloperInfoSection(context: Context) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Geliştirici",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(8.dp))

            // İsim
            SocialRow(
                icon = Icons.Default.Person,
                text = "Salih Türkoğlu",
                onClick = {}
            )

            // --- YENİ EKLENEN: Web Sitesi ---
            SocialRow(
                icon = Icons.Default.Language, // Web için uygun ikon
                text = "salihturkoglu.dev",
                onClick = { openUrl(context, "https://salihturkoglu.dev") }
            )

            // LinkedIn
            SocialRow(
                icon = Icons.Default.Link,
                text = "LinkedIn Profili",
                onClick = { openUrl(context, "https://www.linkedin.com/in/salih-turkoglu/") }
            )

            // GitHub
            SocialRow(
                icon = Icons.Default.Person,
                text = "GitHub Profili",
                onClick = { openUrl(context, "https://github.com/salihturkoglu") }
            )

            // Email
            SocialRow(
                icon = Icons.Default.Email,
                text = "İletişim",
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:turkoglusalih00@gmail.com")
                    }
                    try { context.startActivity(intent) } catch (e: Exception) {}
                }
            )
        }
    }
}

@Composable
fun TmdbAttributionSection(context: Context) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D253F)),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "The Movie Database (TMDB)",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF90CEA1),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bu ürün TMDB API'sini kullanmaktadır ancak TMDB tarafından onaylanmamıştır veya sertifikalandırılmamıştır.",
                //"This product uses the TMDB API but is not endorsed or certified by TMDB."
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { openUrl(context, "https://www.themoviedb.org/") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF01B4E4))
            ) {
                Text("TMDB Sitesini Ziyaret Et")
            }
        }
    }
}

@Composable
fun TechStackSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Kullanılan Teknolojiler",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val techs = listOf("Kotlin", "Jetpack Compose", "Hilt", "Retrofit", "Firebase Auth", "Firestore", "Coil", "Room")

        FlowRowSimple(
            items = techs,
            content = { tech ->
                TechChip(text = tech)
            }
        )
    }
}

@Composable
fun TechChip(text: String) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f), CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SocialRow(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun <T> FlowRowSimple(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        items.forEach { item ->
            content(item)
        }
    }
}

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}