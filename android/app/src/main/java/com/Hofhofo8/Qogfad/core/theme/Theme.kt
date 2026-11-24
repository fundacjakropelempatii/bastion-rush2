package com.Hofhofo8.Qogfad.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BastionColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryDeep,
    onPrimaryContainer = TextPrimary,
    secondary = Secondary,
    onSecondary = BgBase,
    tertiary = Verdant,
    onTertiary = BgBase,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceAlt,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    error = Ember,
    onError = TextPrimary,
    outline = BorderCard,
    outlineVariant = BorderHair
)

data class AppVisuals(
    val hairline: Dp = 1.dp,
    val cardBorder: Dp = 1.dp,
    val surfaceAlpha: Float = 0.92f,
    val safeAccent: Color = Verdant,
    val dangerAccent: Color = Ember,
    val reducedMotion: Boolean = false
)

val LocalAppVisuals = staticCompositionLocalOf { AppVisuals() }

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BastionColorScheme,
        typography = AppTypography,
        content = content
    )
}
