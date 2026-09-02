package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonMagenta,
    onPrimary = Color.White,
    primaryContainer = NeonViolet,
    onPrimaryContainer = Color.White,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = VibeSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = NeonPurple,
    onTertiary = Color.White,
    background = VibeBackground,
    onBackground = TextPrimary,
    surface = VibeSurface,
    onSurface = TextPrimary,
    surfaceVariant = VibeSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = HeartRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek dark vibe aesthetic
    dynamicColor: Boolean = false, // Keep consistent high-craft neon aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
