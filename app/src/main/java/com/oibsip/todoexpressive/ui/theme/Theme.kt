package com.oibsip.todoexpressive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = ExpressivePrimary,
    secondary = ExpressiveSecondary,
    tertiary = ExpressiveTertiary,
    background = ExpressiveBackground,
    surface = ExpressiveSurface,
    onSurface = ExpressiveOnSurface
)

@Composable
fun TodoExpressiveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = AppTypography,
        content = content
    )
}
