package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HermesDarkColorScheme = darkColorScheme(
    primary = HermesCoral,
    onPrimary = HermesTextPrimary,
    primaryContainer = HermesSurfaceElevated,
    onPrimaryContainer = HermesTextPrimary,
    secondary = HermesBlue,
    onSecondary = HermesTextPrimary,
    secondaryContainer = HermesBadgePro,
    onSecondaryContainer = HermesBadgeProText,
    tertiary = HermesAmber,
    background = HermesBackground,
    onBackground = HermesTextPrimary,
    surface = HermesSurface,
    onSurface = HermesTextPrimary,
    surfaceVariant = HermesSurfaceComposer,
    onSurfaceVariant = HermesTextSecondary,
    outline = HermesBorder,
    outlineVariant = HermesDivider,
    error = HermesRed,
    onError = HermesTextPrimary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Hermes default is editorial near-black dark
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HermesDarkColorScheme,
        typography = Typography,
        content = content
    )
}
