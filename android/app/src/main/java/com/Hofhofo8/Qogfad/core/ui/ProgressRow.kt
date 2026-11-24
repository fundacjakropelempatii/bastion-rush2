package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary

@Composable
fun ProgressRow(
    title: String,
    counter: String,
    rewardLabel: String,
    fraction: Float,
    description: String,
    actionLabel: String,
    actionEnabled: Boolean,
    done: Boolean,
    doneLabel: String,
    rewardIcon: Painter,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visuals = LocalAppVisuals.current
    GlassCard(modifier = modifier.fillMaxWidth(), radius = 18.dp, elevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .semantics { stateDescription = description },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                    color = if (done) visuals.safeAccent else Primary,
                    trackColor = SurfaceElevated
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = counter,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = rewardIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rewardLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (done) {
                Text(
                    text = doneLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = visuals.safeAccent
                )
            } else {
                PrimaryButton(
                    label = actionLabel,
                    onClick = onAction,
                    enabled = actionEnabled,
                    height = 48.dp,
                    radius = 14.dp,
                    gradient = listOf(Primary, Color(0xFF4A3FB0)),
                    modifier = Modifier.width(110.dp)
                )
            }
        }
    }
}
