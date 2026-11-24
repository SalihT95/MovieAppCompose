package com.turkoglu.moviecomposeapp.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkoglu.moviecomposeapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Logonun arka plan rengiyle (#010611) pürüzsüz bütünleşen sinematik gradyan
private val SplashBgStart = Color(0xFF010611)
private val SplashBgCenter = Color(0xFF030919)
private val SplashBgEnd = Color(0xFF000309)

private val SplashBackgroundGradient = Brush.verticalGradient(
    colors = listOf(SplashBgStart, SplashBgCenter, SplashBgEnd)
)

@Composable
fun SplashScreen(
    isDataReady: Boolean = true,
    onFinished: () -> Unit
) {
    // Animasyon durumları
    val logoScale = remember { Animatable(0.75f) }
    val logoAlpha = remember { Animatable(0f) }
    val sloganAlpha = remember { Animatable(0f) }
    val progressWidth = remember { Animatable(0f) }
    val screenAlpha = remember { Animatable(1f) }

    val currentDataReady by rememberUpdatedState(isDataReady)
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        // 1. Logo Büyüme ve Netleşme
        launch {
            logoAlpha.animateTo(1f, animationSpec = tween(550, easing = FastOutSlowInEasing))
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        // 2. Slogan Girişi
        delay(250)
        launch {
            sloganAlpha.animateTo(1f, animationSpec = tween(350))
        }

        // 3. İlerleme Çubuğu: 0'dan %75'e akıcı dolsun
        delay(150)
        val progressJob = launch {
            progressWidth.animateTo(0.75f, animationSpec = tween(650, easing = LinearEasing))
        }

        // 4. Verinin hazır olmasını bekle (en fazla 1200ms emniyet süresi)
        val startTime = System.currentTimeMillis()
        while (!currentDataReady && (System.currentTimeMillis() - startTime < 1200)) {
            delay(50)
        }

        // 5. İlerleme çubuğunu %100'e tamamla
        progressJob.cancel()
        progressWidth.animateTo(1f, animationSpec = tween(200, easing = FastOutSlowInEasing))

        // 6. Pürüzsüz Kararma ile Geçiş
        delay(100)
        screenAlpha.animateTo(0f, animationSpec = tween(250, easing = FastOutSlowInEasing))
        currentOnFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBackgroundGradient)
            .alpha(screenAlpha.value),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // --- LOGO ---
            // Logonun içinde zaten "MovieScout" tipografisi olduğu için
            // daire ile kırpmadan, doğal oranında ve tam oturacak şekilde sergiliyoruz.
            Image(
                painter = painterResource(id = R.drawable.movie_scout_app_logo),
                contentDescription = "MovieScout Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- SLOGAN ---
            Text(
                text = "FİLM & DİZİ TAKİP",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 4.sp
                ),
                color = Color(0xFFFFB800).copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(sloganAlpha.value)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // --- SİNEMA YÜKLEME ÇUBUĞU ---
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .alpha(sloganAlpha.value)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressWidth.value)
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFFFB800),
                                    Color(0xFFFF6D00)
                                )
                            )
                        )
                )
            }
        }
    }
}
