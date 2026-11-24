package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.domain.model.Floor

@Composable
fun FloorView(
    floor: Floor,
    width: Dp,
    height: Dp,
    description: String,
    reinforceable: Boolean,
    onReinforce: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visuals = LocalAppVisuals.current
    val damage = (100 - floor.integrity).coerceIn(0, 100)
    val wash = when {
        floor.integrity >= 70 -> Color.Transparent
        floor.integrity >= 35 -> visuals.dangerAccent.copy(alpha = 0.18f)
        else -> visuals.dangerAccent.copy(alpha = 0.34f)
    }
    val shape = RoundedCornerShape(10.dp)
    val tapModifier = if (reinforceable) Modifier.clickable(onClick = onReinforce) else Modifier

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .then(tapModifier)
            .semantics { contentDescription = description }
            .background(SurfaceElevated, shape)
            .border(
                width = if (floor.integrity < 35) 1.5.dp else visuals.hairline,
                color = if (floor.integrity < 35) visuals.dangerAccent else BorderHair,
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(familySprite(floor.family)),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 3.dp),
            contentScale = ContentScale.Fit
        )
        if (wash != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(wash, shape)
            )
        }
        if (damage >= 30) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokes = if (damage >= 66) 4 else 2
                val step = size.width / (strokes + 1)
                repeat(strokes) { index ->
                    val x = step * (index + 1)
                    drawLine(
                        color = visuals.dangerAccent.copy(alpha = 0.75f),
                        start = Offset(x, size.height * 0.16f),
                        end = Offset(x + size.width * 0.06f, size.height * 0.84f),
                        strokeWidth = 2f
                    )
                }
            }
        }
    }
}
