package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Vibesphere Frosted Glass Palette
val FrostedBlack = Color(0xFF0F0F0F)
val FrostedDeepIndigo = Color(0xFF1E1B4B)
val FrostedMidIndigo = Color(0xFF312E81)
val FrostedDarkViolet = Color(0xFF4C1D95)

val VibeBackground = Color(0xFF0F0F0F)
val VibeSurface = Color(0xD92B2930)
val VibeSurfaceVariant = Color(0x33FFFFFF)
val VibeCardBg = Color(0x401E1B4B)
val VibeGlassOverlay = Color(0x26FFFFFF)
val VibeGlassHighlight = Color(0x4DFFFFFF)

// Accents (Frosted Lavender, Violet & Neon Highlights)
val FrostedLavender = Color(0xFFD0BCFF)
val FrostedLilac = Color(0xFFE8DEF8)
val NeonMagenta = Color(0xFFD0BCFF) // Soft Lavender accent
val NeonPurple = Color(0xFFA78BFA)
val NeonViolet = Color(0xFF8B5CF6)
val NeonCyan = Color(0xFF67E8F9)
val NeonPeach = Color(0xFFFDBA74)
val NeonLime = Color(0xFF86EFAC)
val NeonGold = Color(0xFFFDE047)

// Text & Neutral
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xB3FFFFFF)
val TextMuted = Color(0x80FFFFFF)
val BorderSubtle = Color(0x26FFFFFF)
val BorderGlass = Color(0x33FFFFFF)
val HeartRed = Color(0xFFFF5252)

// Gradient Brushes
val FrostedBackdropGradient = Brush.linearGradient(
    listOf(
        Color(0x991E1B4B),
        Color(0x80312E81),
        Color(0x994C1D95)
    )
)

val VibeStoryGradient = Brush.linearGradient(
    listOf(FrostedLavender, NeonPurple, NeonCyan)
)

val VibeReelsGradient = Brush.verticalGradient(
    listOf(
        Color.Transparent,
        Color(0x660F0F0F),
        Color(0xCC0F0F0F),
        Color(0xFF0F0F0F)
    )
)

val VibeHeroGradient = Brush.horizontalGradient(
    listOf(FrostedLavender, NeonViolet, NeonCyan)
)

val VibeGlowGradient = Brush.radialGradient(
    colors = listOf(Color(0x40D0BCFF), Color.Transparent)
)

