package com.Hofhofo8.Qogfad.presentation.levelmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Secondary
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.ConfirmDialog
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.familySprite
import com.Hofhofo8.Qogfad.presentation.common.stageModifierName
import com.Hofhofo8.Qogfad.presentation.common.stageName
import com.Hofhofo8.Qogfad.presentation.common.stageUnlockHint

@Composable
fun LevelMapPanel(onMenu: () -> Unit) {
    val levelMapViewModel: LevelMapViewModel = viewModel(factory = ServiceLocator.factory)
    val state by levelMapViewModel.state.collectAsStateWithLifecycle()

    LevelMapContent(
        state = state,
        onSelect = { levelMapViewModel.select(it) },
        onDismiss = { levelMapViewModel.dismissDialog() },
        onMenu = onMenu
    )
}

@Composable
private fun LevelMapContent(
    state: LevelMapUiState,
    onSelect: (StageEntry) -> Unit,
    onDismiss: () -> Unit,
    onMenu: () -> Unit
) {
    val visuals = LocalAppVisuals.current

    ScreenScaffold(
        overlay = listOf(
            BgBase.copy(alpha = 0.80f),
            BgSink.copy(alpha = 0.90f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.levelmap_title),
                subtitle = stringResource(
                    R.string.levelmap_sub,
                    state.entries.count { it.unlocked },
                    state.entries.size
                ),
                onMenu = onMenu
            )

            state.entries.forEach { entry ->
                val entryDescription = stringResource(
                    if (entry.unlocked) R.string.cd_stage_unlocked else R.string.cd_stage_locked
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier.width(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(
                            modifier = Modifier
                                .width(2.dp)
                                .height(26.dp)
                                .background(BorderCard.copy(alpha = 0.7f))
                        )
                    }
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(entry) },
                        radius = 18.dp,
                        elevation = if (entry.unlocked) 10.dp else 4.dp,
                        border = when {
                            entry.active -> visuals.safeAccent
                            entry.unlocked -> Secondary.copy(alpha = 0.5f)
                            else -> BorderCard
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                                .semantics { stateDescription = entryDescription },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(familySprite(entry.stage.crestFamily)),
                                    contentDescription = null,
                                    modifier = Modifier.size(52.dp),
                                    contentScale = ContentScale.Fit,
                                    alpha = if (entry.unlocked) 1f else 0.18f
                                )
                                if (!entry.unlocked) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_lock),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = TextTertiary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stageName(entry.stage.id),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (entry.unlocked) TextPrimary else TextTertiary
                                )
                                Text(
                                    text = stageModifierName(entry.stage.modifier),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Secondary
                                )
                                Text(
                                    text = stageUnlockHint(entry.stage.id),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextTertiary
                                )
                            }
                            if (entry.active) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_check),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp),
                                    tint = visuals.safeAccent
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.levelmap_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }

    val locked = state.lockedDialog
    if (locked != null) {
        ConfirmDialog(
            title = stageName(locked),
            body = stageUnlockHint(locked),
            confirmLabel = stringResource(R.string.cta_close),
            dismissLabel = stringResource(R.string.cta_cancel),
            onConfirm = onDismiss,
            onDismiss = onDismiss
        )
    }
}
