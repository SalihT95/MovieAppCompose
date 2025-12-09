package com.turkoglu.moviecomposeapp.data.local

import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.OnboardingPage


object OnboardingData {
    val pages = listOf(
        OnboardingPage(
            title = "Keşfet: Popüler ve Trend Filmler",
            description = "Dünya çapında en çok konuşulan, en yeni ve trend içerikleri anında keşfet.",
            imageResId = R.drawable.onboarding_kesfet // Örn: Bir büyüteç veya film makarası ikonu
        ),
        OnboardingPage(
            title = "Favorile: Sevdiğin Filmleri Kaydet",
            description = "İzleme listenizi oluşturun. Bir tıkla favorilerinize ekleyin ve kolayca takip edin.",
            imageResId = R.drawable.onboarding_fav // Örn: Bir kalp veya yer imi ikonu
        ),
        OnboardingPage(
            title = "Detaylı Bilgi: Oyuncular ve Filmografi",
            description = "Filmlerin perde arkasına inin. Oyuncuların kariyerlerini ve tüm filmografisini görün.",
            imageResId = R.drawable.onboarding_cast // Örn: Bir bilgi (i) ikonu veya kişi ikonu
        )
    )
}