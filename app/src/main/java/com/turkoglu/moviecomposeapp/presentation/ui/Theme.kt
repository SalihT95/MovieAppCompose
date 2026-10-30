package com.turkoglu.moviecomposeapp.presentation.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

// 135 Derece Gradyan Renkleri
private val ColorStart = Color(0xFF000000)   // #000000
private val ColorCenter = Color(0xFF070640)  // #070640
private val ColorEnd = Color(0xFF1B0707)     // #1b0707

/**
 * Uygulama genelinde kullanılan 135 derecelik doğrusal gradyan fırçası (Brush).
 * Sol Alttan (BottomStart) Sağ Üste (TopEnd) doğru gider.
 */
val AppBackgroundGradient: Brush = Brush.linearGradient(
    colors = listOf(ColorStart, ColorCenter, ColorEnd),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentRed,
    onPrimary = GrayTextPrimary,
    primaryContainer = DarkSurface,
    onPrimaryContainer = GrayTextPrimary,
    secondary = GrayTextSecondary,
    onSecondary = GrayTextPrimary,
    background = DarkBackground,
    onBackground = GrayTextPrimary,
    surface = DarkSurface,
    onSurface = GrayTextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = AccentRed,
    onPrimary = LightTextPrimary,
    primaryContainer = LightSurface,
    onPrimaryContainer = LightTextPrimary,
    secondary = LightTextSecondary,
    onSecondary = LightTextPrimary,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary
)

@Composable
fun MovieComposeAppTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Android 15 (API 35) ile window.statusBarColor deprecated oldu.
            // WindowInsetsControllerCompat ile status bar ikon rengi ayarlanır.
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
