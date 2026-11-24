package com.Hofhofo8.Qogfad.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Hofhofo8.Qogfad.core.theme.CollapseTop
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.RetreatTop
import com.Hofhofo8.Qogfad.core.theme.Secondary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.PrimaryButton
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.SecondaryButton
import com.Hofhofo8.Qogfad.core.ui.StatCard
import com.Hofhofo8.Qogfad.domain.model.RunOutcome
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.presentation.common.missionTitle
import com.Hofhofo8.Qogfad.presentation.common.outcomeHeadline
import com.Hofhofo8.Qogfad.presentation.common.stageName

@Composable
fun GameOverScreen(
    result: RunResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(result) {
        gameOverViewModel.present(result)
    }

    GameOverContent(state = state, onPlayAgain = onPlayAgain, onMenu = onMenu)
}

private data class ResultStat(val label: String, val value: String, val accentDanger: Boolean)

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val visuals = LocalAppVisuals.current
    val result = state.result
    val retreat = result.outcome == RunOutcome.RETREAT
    val headlineColor = if (retreat) visuals.safeAccent else visuals.dangerAccent
    val recordScale by animateFloatAsState(
        targetValue = if (result.newRecord) 1f else 0.9f,
        animationSpec = tween(520),
        label = "recordScale"
    )

    val stats = buildList {
        if (result.score > 0) {
            add(ResultStat(stringResource(R.string.stat_score), result.score.toString(), false))
        }
        if (result.waves > 0) {
            add(ResultStat(stringResource(R.string.stat_waves_run), result.waves.toString(), false))
        }
        if (result.intercepts > 0) {
            add(
                ResultStat(
                    stringResource(R.string.stat_intercepts),
                    result.intercepts.toString(),
                    true
                )
            )
        }
        if (result.height > 0) {
            add(ResultStat(stringResource(R.string.stat_height), result.height.toString(), false))
        }
    }
    val rows = stats.chunked(2).filter { it.size == 2 }

    ScreenScaffold(
        overlay = listOf(
            (if (retreat) RetreatTop else CollapseTop).copy(alpha = 0.84f),
            BgBase.copy(alpha = 0.92f)
        ),
        bloom = headlineColor.copy(alpha = 0.12f),
        bloomRadius = 700f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = outcomeHeadline(result.outcome),
                    style = MaterialTheme.typography.headlineMedium,
                    color = headlineColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(
                        R.string.result_subline,
                        result.waves,
                        result.height
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Text(
                    text = stageName(result.stage),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }

            GlassCard(modifier = Modifier.fillMaxWidth(), radius = 24.dp, elevation = 16.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (rows.isEmpty()) {
                        Text(
                            text = stringResource(R.string.result_empty),
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        rows.forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                row.forEach { stat ->
                                    StatCard(
                                        label = stat.label,
                                        value = stat.value,
                                        accent = if (stat.accentDanger) {
                                            visuals.dangerAccent
                                        } else {
                                            visuals.safeAccent
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_5),
                            contentDescription = stringResource(R.string.cd_shard_token),
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.result_shards, result.shards),
                            style = MaterialTheme.typography.labelLarge,
                            color = Secondary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.result_balance, state.shardBalance),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextTertiary
                        )
                    }
                }
            }

            if (result.newRecord) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .graphicsLayer {
                            scaleX = recordScale
                            scaleY = recordScale
                        },
                    shape = RoundedCornerShape(14.dp),
                    color = visuals.safeAccent.copy(alpha = 0.18f),
                    border = BorderStroke(visuals.cardBorder, visuals.safeAccent)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = visuals.safeAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.result_record, state.bestScore),
                            style = MaterialTheme.typography.labelSmall,
                            color = visuals.safeAccent
                        )
                    }
                }
            }

            if (result.completed.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    result.completed.forEach { mission ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = visuals.safeAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = missionTitle(mission),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.result_hint),
                    modifier = Modifier.padding(bottom = 10.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center
                )
                PrimaryButton(
                    label = stringResource(R.string.cta_play_again),
                    onClick = onPlayAgain,
                    modifier = Modifier.fillMaxWidth(),
                    height = 60.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                SecondaryButton(
                    label = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(R.drawable.icon_home)
                )
            }
        }
    }
}
