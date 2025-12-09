package com.turkoglu.moviecomposeapp.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.util.AvatarUtils

@Composable
fun ProfileImage(
    avatarKey: String?, // Firestore'dan gelen veri (örn: "cat" veya "https://...")
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    // 1. Durum: Bu bir Yerel Avatar mı? (AvatarUtils listesinde var mı?)
    val localResId = if (avatarKey != null) AvatarUtils.getDrawableId(avatarKey) else null

    // getDrawableId fonksiyonu bulamazsa "Ghost" döndürür.
    // Ancak biz "Ghost" mu yoksa gerçekten URL mi olduğunu ayırt etmeliyiz.
    // Bu yüzden AvatarUtils.avatarMap içinde key var mı diye kontrol etmek daha güvenli.

    val isLocalAvatar = avatarKey != null && AvatarUtils.avatarMap.containsKey(avatarKey)

    if (isLocalAvatar) {
        // --- YEREL RESİM ---
        Image(
            painter = painterResource(id = localResId ?: R.drawable.ghost),
            contentDescription = "Profile Avatar",
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        // --- İNTERNET RESMİ (URL) ---
        // URL Hazırlığı (TMDB veya Firebase)
        val finalUrl = remember(avatarKey) {
            when {
                avatarKey.isNullOrEmpty() -> null
                avatarKey.startsWith("http") -> avatarKey // Tam link
                else -> "https://image.tmdb.org/t/p/w500$avatarKey" // TMDB linki (varsa)
            }
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(finalUrl ?: R.drawable.ic_placeholder) // Null ise placeholder
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .crossfade(true)
                .build(),
            contentDescription = "Profile Remote",
            contentScale = contentScale,
            modifier = modifier
        )
    }
}