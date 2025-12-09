package com.turkoglu.moviecomposeapp.presentation.detail.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.presentation.Screen
import com.turkoglu.moviecomposeapp.presentation.component.CastItem
import com.turkoglu.moviecomposeapp.presentation.component.CircularBackButtons
import com.turkoglu.moviecomposeapp.presentation.component.CircularFavoriteButtons
import com.turkoglu.moviecomposeapp.presentation.component.ExpandableText
import com.turkoglu.moviecomposeapp.presentation.component.FragmanButton
import com.turkoglu.moviecomposeapp.presentation.component.GenreChipsRow
import com.turkoglu.moviecomposeapp.presentation.detail.DetailScreenViewModel
import com.turkoglu.moviecomposeapp.presentation.ui.AppBackgroundGradient
import com.turkoglu.moviecomposeapp.presentation.user.UserViewModel
import com.turkoglu.moviecomposeapp.util.Constants
import kotlinx.coroutines.launch

@SuppressLint("SupportAnnotationUsage", "StateFlowValueCalledInComposition")
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    // viewModelFav: FavViewModel artık kullanılmıyor, DetailScreenViewModel içinde hallediliyor
    userViewModel: UserViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Film verisi
    val film = viewModel.state.value

    // Video URL Güvenliği
    val videoUrl = viewModel.fragmanState.value.videoUrl
    val intent = Intent(Intent.ACTION_VIEW, (videoUrl ?: "").toUri())
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    // Kullanıcı ve Favori Durumu
    val currentUser by userViewModel.currentUser.collectAsState()
    val isFavorite by viewModel.isFavorite // ViewModel'den gelen Firebase state'i

    val castState = viewModel.castState.value

    // Sayfa açıldığında favori durumunu kontrol et
    LaunchedEffect(film.imdbId) {
        if (film.imdbId != 0) {
            viewModel.checkFavoriteStatus(film.imdbId)
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
            //.padding(16.dp) // Padding'i buradan kaldırdık, içeriğe özel vereceğiz
        ) {
            // --- TOP BAR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Top bar için padding
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularBackButtons(
                    onBackClick = { navController.popBackStack() },
                    onHomeClick = { navController.navigate("Home") })

                // Video varsa butonu göster
                if (!videoUrl.isNullOrEmpty()) {
                    FragmanButton {
                        try {
                            launcher.launch(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Video açılamadı", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // Favori Butonu
                CircularFavoriteButtons(
                    isLiked = isFavorite,
                    onClick = { _ ->
                        coroutineScope.launch {
                            // 1. Misafir Kontrolü
                            if (currentUser == null || currentUser?.isGuest == true) {
                                Toast.makeText(
                                    context,
                                    "Favorilere eklemek için giriş yapmalısınız",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@launch
                            }

                            // 2. Toggle İşlemi (ViewModel üzerinden Firebase'e gider)
                            viewModel.toggleFavorite(film)

                            // 3. Kullanıcıya Bilgi Ver (Ters mantık: Şu an favoriyse siliniyordur)
                            val message =
                                if (isFavorite) "Favorilerden kaldırıldı" else "Favorilere eklendi"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            // --- İÇERİK ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.padding(top = 12.dp) // Top bar ile görsel arasına boşluk bırakmaya gerek yok
                    .verticalScroll(rememberScrollState())
            ) {
                // --- FİLM GÖRSELLERİ (BACKDROP + POSTER) ---

                // URL'leri hazırla
                val backdropUrl = remember(film.backdropPath) {
                    film.backdropPath?.let { path ->
                        if (path.startsWith("http")) path else "https://image.tmdb.org/t/p/w780$path"
                    }
                }
                val posterUrl = remember(film.posterPath) {
                    film.posterPath?.let { path ->
                        if (path.startsWith("http")) path else "https://image.tmdb.org/t/p/w500$path"
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Toplam görsel alanı yüksekliği
                ) {
                    // 1. KATMAN: Arka Plan (Backdrop)
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(backdropUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Backdrop Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            // Alt köşeleri yuvarlat (örnekteki gibi)
                            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                            .alpha(0.8f) // Karartma efekti için şeffaflık
                    )

                    // 2. KATMAN: Ön Plan (Poster)
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(posterUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Poster Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.BottomStart) // Sol alta hizala
                            .padding(
                                start = 16.dp,
                                bottom = 16.dp
                            ) // Kenarlardan boşluk bırak (örnekteki gibi biraz içeride)
                            .width(100.dp) // Poster genişliği
                            .height(150.dp) // Poster yüksekliği
                            .clip(RoundedCornerShape(12.dp)) // Posterin köşelerini yuvarlat
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- FİLM BİLGİLERİ ---
                Column(modifier = Modifier.padding(horizontal = 16.dp)) { // Bilgiler için yanlardan padding
                    Text(
                        text = film.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "⭐ %.1f".format(film.voteAverage), color = Color.Yellow)
                        Text(
                            text = film.releaseDate,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    GenreChipsRow(
                        textColor = MaterialTheme.colorScheme.onBackground,
                        genreList = film.genres.map { it.name }
                    ) {
                        navController.navigate("ViewAll/$it")
                    }

                    ExpandableText(
                        text = film.overview,
                        modifier = Modifier.fillMaxWidth(),
                        minimizedMaxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ---------------------------Cast -----------------------------------
                    Text(
                        text = "Cast",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } // Film Bilgileri Column Sonu

                // Cast Listesi (Padding'in dışında kalsın, kenarlara kadar gitsin)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp) // Yanlardan boşluk
                ) {
                    itemsIndexed(castState.cast, key = { _, cast -> cast.id }) { _, cast ->
                        CastItem(
                            castName = cast.name,
                            castImageUrl = "${Constants.IMAGE_BASE_URL}/${cast.profile_path}",
                            onClick = {
                                navController.navigate(
                                    Screen.Cast.route.replace(
                                        "{personId}",
                                        cast.id.toString()
                                    )
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}