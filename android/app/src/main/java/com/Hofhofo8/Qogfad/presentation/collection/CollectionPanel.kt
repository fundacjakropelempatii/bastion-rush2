package com.Hofhofo8.Qogfad.presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.ConfirmDialog
import com.Hofhofo8.Qogfad.core.ui.LockedTile
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.familySprite
import com.Hofhofo8.Qogfad.presentation.common.relicName
import com.Hofhofo8.Qogfad.presentation.common.relicTrait

@Composable
fun CollectionPanel(onMenu: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by collectionViewModel.state.collectAsStateWithLifecycle()
    CollectionContent(
        state = state,
        onOpen = { collectionViewModel.open(it) },
        onDismiss = { collectionViewModel.dismiss() },
        onMenu = onMenu
    )
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onOpen: (RelicEntry) -> Unit,
    onDismiss: () -> Unit,
    onMenu: () -> Unit
) {
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.collection_title),
                subtitle = stringResource(
                    R.string.collection_counter,
                    state.unlockedCount,
                    state.total
                ),
                onMenu = onMenu
            )

            if (state.empty) {
                Text(
                    text = stringResource(R.string.collection_empty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            state.entries.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { entry ->
                        LockedTile(
                            icon = painterResource(familySprite(entry.relic.family)),
                            title = relicName(entry.relic.id),
                            caption = if (entry.unlocked) {
                                relicTrait(entry.relic.id)
                            } else {
                                stringResource(
                                    R.string.collection_requirement,
                                    entry.relic.dropsRequired
                                )
                            },
                            description = stringResource(
                                R.string.cd_relic,
                                entry.drops,
                                entry.relic.dropsRequired
                            ),
                            unlocked = entry.unlocked,
                            iconSize = 34.dp,
                            onClick = { onOpen(entry) },
                            modifier = Modifier
                                .weight(1f)
                                .height(112.dp)
                        )
                    }
                    if (row.size < 3) {
                        Spacer(modifier = Modifier.weight((3 - row.size).toFloat()))
                    }
                }
            }

            Text(
                text = stringResource(R.string.collection_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }

    val detail = state.detail
    if (detail != null) {
        ConfirmDialog(
            title = relicName(detail),
            body = relicTrait(detail),
            confirmLabel = stringResource(R.string.cta_close),
            dismissLabel = stringResource(R.string.cta_cancel),
            onConfirm = onDismiss,
            onDismiss = onDismiss
        )
    }
}
