package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.SurfaceBase
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary

@Composable
fun LockedTile(
    icon: Painter,
    title: String,
    caption: String,
    description: String,
    unlocked: Boolean,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visuals = LocalAppVisuals.current
    GlassCard(
        modifier = modifier.clickable(onClick = onClick),
        radius = 16.dp,
        elevation = if (unlocked) 10.dp else 4.dp,
        container = SurfaceBase,
        border = if (unlocked) visuals.safeAccent.copy(alpha = 0.55f) else BorderCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .semantics { stateDescription = description },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit,
                alpha = if (unlocked) 1f else 0.16f
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (unlocked) TextPrimary else TextTertiary,
                textAlign = TextAlign.Center
            )
            if (!unlocked) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TextTertiary
                )
            }
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
        }
    }
}
