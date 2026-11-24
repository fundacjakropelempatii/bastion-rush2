package com.Hofhofo8.Qogfad.presentation.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import com.Hofhofo8.Qogfad.core.theme.Secondary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.ProgressRow
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.domain.model.Chest
import com.Hofhofo8.Qogfad.presentation.common.chestName

@Composable
fun RewardsPanel(onMenu: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by rewardsViewModel.state.collectAsStateWithLifecycle()
    RewardsContent(state = state, onClaim = { rewardsViewModel.claim(it) }, onMenu = onMenu)
}

@Composable
private fun RewardsContent(
    state: RewardsUiState,
    onClaim: (Chest) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.rewards_title),
                subtitle = stringResource(R.string.rewards_disclaimer),
                onMenu = onMenu
            )

            GlassCard(modifier = Modifier.fillMaxWidth(), radius = 20.dp, elevation = 10.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = stringResource(R.string.cd_shard_token),
                        modifier = Modifier.size(44.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = state.shards.toString(),
                        style = MaterialTheme.typography.displaySmall,
                        color = Secondary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.rewards_balance_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
            }

            if (state.nothingClaimable) {
                Text(
                    text = stringResource(R.string.rewards_empty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            state.entries.forEach { entry ->
                ProgressRow(
                    title = chestName(entry.chest.id),
                    counter = stringResource(
                        R.string.rewards_counter,
                        state.shards.coerceAtMost(entry.chest.cost),
                        entry.chest.cost
                    ),
                    rewardLabel = stringResource(R.string.rewards_relics, entry.chest.relics.size),
                    fraction = if (entry.claimed) 1f else entry.fraction,
                    description = stringResource(R.string.cd_chest, entry.chest.cost),
                    actionLabel = stringResource(R.string.cta_claim),
                    actionEnabled = entry.affordable,
                    done = entry.claimed,
                    doneLabel = stringResource(R.string.state_claimed),
                    rewardIcon = painterResource(R.drawable.sprite_5),
                    onAction = { onClaim(entry.chest) }
                )
            }

            Text(
                text = stringResource(R.string.rewards_how),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }
}
