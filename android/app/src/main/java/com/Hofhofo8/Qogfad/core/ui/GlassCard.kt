package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.SurfaceAlt

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Dp = 20.dp,
    elevation: Dp = 10.dp,
    container: Color = SurfaceAlt,
    border: Color = BorderCard,
    content: @Composable BoxScope.() -> Unit
) {
    val visuals = LocalAppVisuals.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        color = container.copy(alpha = visuals.surfaceAlpha),
        shadowElevation = elevation,
        border = BorderStroke(visuals.cardBorder, border)
    ) {
        Box(content = content)
    }
}
