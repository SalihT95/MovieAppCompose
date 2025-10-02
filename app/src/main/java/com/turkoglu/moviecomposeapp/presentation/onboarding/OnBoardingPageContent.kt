package com.turkoglu.moviecomposeapp.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.OnboardingPage
import com.turkoglu.moviecomposeapp.presentation.ui.MovieComposeAppTheme

@Composable
fun OnboardingPageContent(page: OnboardingPage, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Görüntü (Görsellerinizin de beyaz/kontrastlı olmasını öneririm)
        Image(
            painter = painterResource(id = page.imageResId),
            contentDescription = page.title,
            // Renge Tint ekleyerek ikonun beyaz olmasını sağlayabiliriz (Eğer resim vektör/ikon ise)
            // colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(250.dp)
        )
        Spacer(Modifier.height(48.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            textAlign = TextAlign.Center,
            color = Color.White // Başlık Rengi: Beyaz
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.8f) // Açıklama Rengi: Hafif soluk beyaz
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000) // Preview with a dark background
@Composable
fun OnboardingPageContentPreview() {
    MovieComposeAppTheme {
        OnboardingPageContent(
            page = OnboardingPage(
                title = "Welcome to the App",
                description = "Discover the best movies and TV shows all in one place.",
                imageResId = R.drawable.ic_launcher_foreground // Use a placeholder drawable for preview
            )
        )
    }
}

//@Composable
//fun OnboardingPageContent(
//    page: OnboardingPage,
//    modifier: Modifier = Modifier // Use an optional modifier parameter
//) {
//    Column(
//        // Pass the modifier from the function parameter to allow external modifications
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = 24.dp), // Use horizontal padding
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Image
//        Image(
//            painter = painterResource(id = page.imageResId),
//            contentDescription = page.title,
//            modifier = Modifier.size(250.dp)
//        )
//
//        Spacer(Modifier.height(48.dp))
//
//        // Title
//        Text(
//            text = page.title,
//            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
//            textAlign = TextAlign.Center,
//            color = MaterialTheme.colorScheme.onBackground
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        // Description
//        Text(
//            text = page.description,
//            style = MaterialTheme.typography.bodyLarge,
//            textAlign = TextAlign.Center,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//    }
//}