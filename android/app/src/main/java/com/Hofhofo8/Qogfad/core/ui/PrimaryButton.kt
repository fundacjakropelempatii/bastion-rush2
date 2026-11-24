package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.PrimaryDeep
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary

@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    radius: Dp = 18.dp,
    gradient: List<Color> = listOf(Primary, PrimaryDeep),
    glow: Color = Primary
) {
    val visuals = LocalAppVisuals.current
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val target = if (pressed && enabled && !visuals.reducedMotion) 0.97f else 1f
    val scale by animateFloatAsState(
        targetValue = target,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMedium),
        label = "primaryScale"
    )
    val shape = RoundedCornerShape(radius)
    val fill = if (enabled) gradient else listOf(SurfaceElevated, SurfaceElevated)

    Button(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 14.dp else 0.dp,
                shape = shape,
                ambientColor = glow,
                spotColor = glow
            )
            .background(Brush.horizontalGradient(fill), shape),
        enabled = enabled,
        shape = shape,
        interactionSource = interaction,
        elevation = null,
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextTertiary
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}
