package com.Hofhofo8.Qogfad.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.Hofhofo8.Qogfad.core.navigation.Screen
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.BgSoft
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.PrimaryDeep
import com.Hofhofo8.Qogfad.core.theme.Secondary
import com.Hofhofo8.Qogfad.core.theme.SurfaceAlt
import com.Hofhofo8.Qogfad.core.theme.SurfaceBase
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.StatCard
import com.Hofhofo8.Qogfad.presentation.common.stageModifierName
import com.Hofhofo8.Qogfad.presentation.common.stageName

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onOpen: (Screen) -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    MenuContent(state = state, onPlay = onPlay, onOpen = onOpen)
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpen: (Screen) -> Unit
) {
    val visuals = LocalAppVisuals.current
    ScreenScaffold(
        overlay = listOf(
            BgBase.copy(alpha = 0.58f),
            BgSoft.copy(alpha = 0.74f),
            BgSink.copy(alpha = 0.92f)
        ),
        bloom = Primary.copy(alpha = 0.16f),
        bloomRadius = 820f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            MenuHeader(shards = state.shards)

            if (state.statsVisible) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_best_height),
                        value = state.bestHeight.toString(),
                        accent = visuals.safeAccent,
                        modifier = Modifier.weight(1f),
                        description = stringResource(R.string.cd_best_height, state.bestHeight)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_waves),
                        value = state.wavesCleared.toString(),
                        accent = Secondary,
                        modifier = Modifier.weight(1f),
                        description = stringResource(R.string.cd_waves_cleared, state.wavesCleared)
                    )
                }
            } else {
                GlassCard(modifier = Modifier.fillMaxWidth(), radius = 16.dp, elevation = 6.dp) {
                    Text(
                        text = stringResource(R.string.menu_first_siege),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            DestinationGrid(
                missionsReady = state.missionsReady > 0,
                rewardsReady = state.rewardsReady,
                relicsUnlocked = state.relicsUnlocked,
                stagesUnlocked = state.stagesUnlocked,
                onOpen = onOpen
            )

            StageStrip(state = state)

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.menu_hint),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
                PlayTile(state = state, onPlay = onPlay)
            }
        }
    }
}

@Composable
private fun MenuHeader(shards: Int) {
    val visuals = LocalAppVisuals.current
    val shardDescription = stringResource(R.string.cd_shard_balance, shards)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.cd_brand_crest),
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.brand_line_one),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.brand_line_two),
                    style = MaterialTheme.typography.labelSmall,
                    color = Secondary
                )
            }
        }
        Surface(
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceAlt.copy(alpha = 0.90f),
            border = BorderStroke(visuals.hairline, BorderHair)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .semantics { stateDescription = shardDescription },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_shard_token),
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = shards.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Secondary
                )
            }
        }
    }
}

@Composable
private fun DestinationGrid(
    missionsReady: Boolean,
    rewardsReady: Boolean,
    relicsUnlocked: Int,
    stagesUnlocked: Int,
    onOpen: (Screen) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DestinationTile(
                icon = R.drawable.ic_levels,
                label = stringResource(R.string.tile_levels),
                badge = false,
                caption = stringResource(R.string.caption_stages, stagesUnlocked),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.LevelMap) }
            )
            DestinationTile(
                icon = R.drawable.ic_missions,
                label = stringResource(R.string.tile_missions),
                badge = missionsReady,
                caption = stringResource(R.string.caption_orders),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.Missions) }
            )
            DestinationTile(
                icon = R.drawable.ic_rewards,
                label = stringResource(R.string.tile_rewards),
                badge = rewardsReady,
                caption = stringResource(R.string.caption_banners),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.Rewards) }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DestinationTile(
                icon = R.drawable.ic_collection,
                label = stringResource(R.string.tile_collection),
                badge = false,
                caption = stringResource(R.string.caption_relics, relicsUnlocked),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.Collection) }
            )
            DestinationTile(
                icon = R.drawable.ic_tutorial,
                label = stringResource(R.string.tile_tutorial),
                badge = false,
                caption = stringResource(R.string.caption_four_steps),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.Tutorial) }
            )
            DestinationTile(
                icon = R.drawable.ic_settings,
                label = stringResource(R.string.tile_settings),
                badge = false,
                caption = stringResource(R.string.caption_six_options),
                modifier = Modifier.weight(1f),
                onClick = { onOpen(Screen.Settings) }
            )
        }
    }
}

@Composable
private fun DestinationTile(
    icon: Int,
    label: String,
    caption: String,
    badge: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val visuals = LocalAppVisuals.current
    Box(modifier = modifier) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp),
            shape = RoundedCornerShape(18.dp),
            color = SurfaceBase.copy(alpha = visuals.surfaceAlpha),
            border = BorderStroke(visuals.cardBorder, BorderHair),
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp),
                    tint = Secondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary
                )
                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }
        if (badge) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(8.dp)
                    .background(visuals.dangerAccent, CircleShape)
            )
        }
    }
}

@Composable
private fun StageStrip(state: MenuUiState) {
    val visuals = LocalAppVisuals.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceAlt.copy(alpha = 0.80f),
        border = BorderStroke(visuals.hairline, BorderHair)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.menu_active_stage, stageName(state.activeStage)),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stageModifierName(state.activeModifier),
                style = MaterialTheme.typography.labelSmall,
                color = Secondary
            )
        }
    }
}

@Composable
private fun PlayTile(state: MenuUiState, onPlay: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Button(
        onClick = onPlay,
        modifier = Modifier
            .fillMaxWidth()
            .height(132.dp)
            .shadow(16.dp, shape, ambientColor = Primary, spotColor = Primary)
            .background(
                Brush.linearGradient(listOf(Primary.copy(alpha = 0.92f), PrimaryDeep)),
                shape
            ),
        shape = shape,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.cta_play),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.menu_stage_line, stageName(state.activeStage)),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary.copy(alpha = 0.82f)
                )
                Text(
                    text = stringResource(R.string.menu_record_line, state.bestScore),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary.copy(alpha = 0.62f)
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(86.dp),
                contentScale = ContentScale.Fit,
                alpha = 0.62f
            )
        }
    }
}
