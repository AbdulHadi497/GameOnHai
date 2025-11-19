package com.project.gameonhai.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ARENA-X TYPOGRAPHY – Neon Sport, Scoreboard Inspired
val Typography = Typography(

    // Massive screen titles – dramatic and narrow
    displayLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 52.sp,
        letterSpacing = (-1.2).sp,   // Tight & aggressive
        lineHeight = 56.sp
    ),

    // Large section headers – scoreboard style
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        letterSpacing = (-0.6).sp,
        lineHeight = 42.sp
    ),

    // Card titles – strong, athletic
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 28.sp
    ),

    // Subtitle / secondary headers
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.3.sp,
        lineHeight = 24.sp
    ),

    // Body text – clean, readable, bright
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 24.sp
    ),

    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),

    // Buttons, chips, bottom nav – crisp futuristic all-caps feel
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 1.6.sp,   // Still sporty but less extreme
        lineHeight = 20.sp
    ),

    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 1.4.sp
    ),

    // Small labels & tags – perfect for slot chips
    labelSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.6.sp
    )
)
