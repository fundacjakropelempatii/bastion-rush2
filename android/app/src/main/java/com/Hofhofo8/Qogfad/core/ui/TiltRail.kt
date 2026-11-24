package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.Amber
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated

@Composable
fun tiltColor(tilt: Int): Color {
    val visuals = LocalAppVisuals.current
    return when {
        tilt < 50 -> visuals.safeAccent
        tilt < 80 -> lerp(Amber, visuals.dangerAccent, (tilt - 50) / 30f)
        else -> visuals.dangerAccent
    }
}

@Composable
fun TiltRail(
    tilt: Int,
    description: String,
    modifier: Modifier = Modifier,
    width: Dp = 16.dp,
    height: Dp = 320.dp
) {
    val fraction by animateFloatAsState(
        targetValue = (tilt / 100f).coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 180f),
        label = "tiltFill"
    )
    val color = tiltColor(tilt)
    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .semantics {
                stateDescription = description
                liveRegion = LiveRegionMode.Polite
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = CornerRadius(size.width / 2f, size.width / 2f)
            drawRoundRect(
                color = SurfaceElevated,
                size = size,
                cornerRadius = radius
            )
            val fillHeight = size.height * fraction
            if (fillHeight > 1f) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(0f, size.height - fillHeight),
                    size = Size(size.width, fillHeight),
                    cornerRadius = radius
                )
            }
            val bobY = (size.height - fillHeight).coerceIn(6f, size.height - 6f)
            drawCircle(
                color = Color.White,
                radius = size.width * 0.42f,
                center = Offset(size.width / 2f, bobY)
            )
            drawCircle(
                color = color,
                radius = size.width * 0.26f,
                center = Offset(size.width / 2f, bobY)
            )
        }
    }
}
