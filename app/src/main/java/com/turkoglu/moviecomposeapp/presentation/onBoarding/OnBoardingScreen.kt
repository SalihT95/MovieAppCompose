package com.turkoglu.moviecomposeapp.presentation.onBoarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.rememberPagerState
import com.turkoglu.moviecomposeapp.data.local.OnboardingData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = OnboardingData.pages
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pages.size - 1

    // Ana Arka Plan Box'ı (Gradıyan Eklemek İçin)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A1F)) // Koyu Temel Renk
    ) {
        // İsteğe Bağlı: Daha derin bir sinematik gradyan için
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF03001C).copy(alpha = 0.8f), // Koyu Mavi Üst
                            Color(0xFF0A000A) // Neredeyse Siyah Alt
                        )
                    )
                )
        )

        Scaffold(
            // Arka planı transparan yapıyoruz ki alttaki gradyan görünsün
            containerColor = Color.Transparent,
            contentColor = Color.White, // İçerik (Yazı) rengini beyaz yap
            bottomBar = {
                // BottomBar içeriği (İleri Butonu ve Gösterge)
                Column(
                    // ... (Paddingler aynı kalabilir) ...
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PageIndicator(
                        pageSize = pages.size,
                        currentPage = pagerState.currentPage
                    )
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = {
                            // ... (Navigasyon mantığı aynı) ...
                        },
                        modifier = Modifier.fillMaxWidth(),
                        // Butonun rengini parlak bir kontrast rengi yapalım
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary // Örn: Uygulamanın ana rengi
                        )
                    ) {
                        Text(
                            text = if (isLastPage) "Başla" else "İleri",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->
            // Sayfa Kaydırıcı
            HorizontalPager(
                // ... (PagerState ve Modifier aynı kalır) ...
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { pageIndex ->
                OnboardingPageContent(page = pages[pageIndex])
            }
        }
    }
}