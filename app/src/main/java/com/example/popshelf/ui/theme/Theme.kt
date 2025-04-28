package com.example.popshelf.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9AA0FF),
    onPrimary = Color(0xFF1C1C3A),
    primaryContainer = Color(0xFF3C3F66),
    secondary = Color(0xFF72B2F5),
    onSecondary = Color(0xFF000D1A),
    background = Color(0xFF181A2C),
    onBackground = Color(0xFFE4E6FF),
    surface = Color(0xFF20223A),
    onSurface = Color(0xFFE4E6FF),
    surfaceVariant = Color(0xFF2A2D4F),
    onSurfaceVariant = Color(0xFFE0E2F0),
    outline = Color(0xFF5F6387),
    inverseSurface = Color(0xFFE4E6FF),
    inverseOnSurface = Color(0xFF1B1C2E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5E60CE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE0E0FF),
    secondary = Color(0xFF5390D9),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFAFAFF),
    onBackground = Color(0xFF2A2A45),
    surface = Color(0xFFF2F3FF),
    onSurface = Color(0xFF2A2A45)
)

@Composable
fun PopshelfTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}