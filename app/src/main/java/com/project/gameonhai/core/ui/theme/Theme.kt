package com.project.gameonhai.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ======================
//  ARENAX – Neon Court Theme
// ======================

// Signatures
private val NeonGreen = Color(0xFF32FF4A)
private val ElectricTeal = Color(0xFF00FFD5)
private val ArenaMagenta = Color(0xFFFF007A)

// Stadium blacks
private val PureBlack = Color(0xFF000000)
private val CarbonBlack = Color(0xFF0A0A0A)
private val Graphite = Color(0xFF111111)

// Text tones
private val GlowWhite = Color(0xFFE9FFE9)
private val SoftGreenText = Color(0xFFB6FFB6)

// =======================
// Light Theme
// =======================
private val ArenaLightScheme = lightColorScheme(
    primary = NeonGreen,
    secondary = ElectricTeal,
    tertiary = ArenaMagenta,

    background = PureBlack,
    surface = Graphite,
    surfaceVariant = CarbonBlack,

    onPrimary = PureBlack,
    onSecondary = PureBlack,
    onBackground = GlowWhite,
    onSurface = SoftGreenText,

    primaryContainer = Color(0xFF004D1F),
    onPrimaryContainer = NeonGreen,
    secondaryContainer = Color(0xFF003D3D),
    onSecondaryContainer = ElectricTeal,

    outline = NeonGreen.copy(alpha = 0.8f),
    inverseSurface = NeonGreen.copy(alpha = 0.1f),
    inverseOnSurface = NeonGreen,
    inversePrimary = PureBlack,

    scrim = PureBlack.copy(alpha = 0.6f)
)

// =======================
// Dark Theme (Main Arena Mode)
// =======================
private val ArenaDarkScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = ElectricTeal,
    tertiary = ArenaMagenta,

    background = PureBlack,
    surface = CarbonBlack,
    surfaceVariant = Graphite,

    onPrimary = PureBlack,
    onSecondary = PureBlack,
    onBackground = GlowWhite,
    onSurface = SoftGreenText,

    primaryContainer = Color(0xFF00551F),
    onPrimaryContainer = NeonGreen,
    secondaryContainer = Color(0xFF004040),
    onSecondaryContainer = ElectricTeal,

    outline = NeonGreen,
    scrim = PureBlack.copy(alpha = 0.7f)
)

// =======================
// Composable Theme
// =======================
@Composable
fun CourtBookingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ArenaDarkScheme else ArenaLightScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
