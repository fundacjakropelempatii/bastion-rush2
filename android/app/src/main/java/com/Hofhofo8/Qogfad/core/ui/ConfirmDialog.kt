package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.Hofhofo8.Qogfad.core.theme.SurfaceAlt
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary

@Composable
fun ConfirmDialog(
    title: String,
    body: String,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfaceAlt,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = body, style = MaterialTheme.typography.bodyLarge)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, style = MaterialTheme.typography.labelSmall)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissLabel, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
        }
    )
}
