package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.domain.model.BlockCard

@Composable
fun BlockCardView(
    card: BlockCard,
    familyLabel: String,
    flawLabel: String,
    statLine: String,
    description: String,
    selected: Boolean,
    detailed: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visuals = LocalAppVisuals.current
    val scale by animateFloatAsState(
        targetValue = if (selected && !visuals.reducedMotion) 1.04f else 1f,
        label = "cardScale"
    )
    val deltaColor = when {
        card.tiltDelta <= 0 -> visuals.safeAccent
        card.tiltDelta >= 10 -> visuals.dangerAccent
        else -> TextSecondary
    }
    Surface(
        modifier = modifier
            .heightIn(min = 92.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(onClick = onSelect)
            .semantics { stateDescription = description },
        shape = RoundedCornerShape(16.dp),
        color = if (selected) Primary.copy(alpha = 0.18f) else SurfaceElevated,
        border = BorderStroke(if (selected) 2.dp else visuals.hairline, if (selected) Primary else BorderHair)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(familySprite(card.family)),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = familyLabel,
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            if (detailed) {
                Text(
                    text = statLine,
                    style = MaterialTheme.typography.labelSmall,
                    color = deltaColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = flawLabel,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .background(BorderHair, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }
    }
}
