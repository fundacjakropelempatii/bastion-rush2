package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    accent: Color = BorderCard,
    icon: Painter? = null
) {
    val visuals = LocalAppVisuals.current
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(visuals.cardBorder, accent),
        contentPadding = PaddingValues(horizontal = 18.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        if (icon != null) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
