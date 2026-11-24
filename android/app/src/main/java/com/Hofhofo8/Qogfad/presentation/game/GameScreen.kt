package com.Hofhofo8.Qogfad.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.config.GameConfig
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.Ember
import com.Hofhofo8.Qogfad.core.theme.EmberDeep
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.Secondary
import com.Hofhofo8.Qogfad.core.theme.SurfaceAlt
import com.Hofhofo8.Qogfad.core.theme.SurfaceBase
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.SurfaceSilhouette
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.BlockCardView
import com.Hofhofo8.Qogfad.core.ui.FloorView
import com.Hofhofo8.Qogfad.core.ui.PrimaryButton
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.SecondaryButton
import com.Hofhofo8.Qogfad.core.ui.TiltRail
import com.Hofhofo8.Qogfad.core.ui.tiltColor
import com.Hofhofo8.Qogfad.domain.model.BlockCard
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.presentation.common.familyName
import com.Hofhofo8.Qogfad.presentation.common.flawName
import com.Hofhofo8.Qogfad.presentation.common.stageName

private val FloorHeight = 34.dp
private val RaiderSize = 46.dp

@Composable
fun GameScreen(
    runId: Int,
    onFinish: (RunResult) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(runId) {
        gameViewModel.beginRun(runId)
    }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { result -> onFinish(result) }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onRetreat = { gameViewModel.retreat() },
        onSelect = { gameViewModel.selectCard(it) },
        onDrop = { gameViewModel.dropBlock() },
        onReinforce = { gameViewModel.reinforce(it) },
        onIntercept = { gameViewModel.intercept(it) },
        onKeystone = {
            if (state.keystoneBanked != null) gameViewModel.deployKeystone()
            else gameViewModel.bankKeystone()
        }
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onRetreat: () -> Unit,
    onSelect: (Int) -> Unit,
    onDrop: () -> Unit,
    onReinforce: (Int) -> Unit,
    onIntercept: (Int) -> Unit,
    onKeystone: () -> Unit
) {
    ScreenScaffold(
        overlay = listOf(
            BgSink.copy(alpha = 0.72f),
            BgBase.copy(alpha = 0.42f),
            BgSink.copy(alpha = 0.84f)
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GameHeader(state = state, onRetreat = onRetreat)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                SpireArea(
                    state = state,
                    onReinforce = onReinforce,
                    onIntercept = onIntercept
                )

                if (state.numericTilt) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 14.dp, top = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceAlt.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, BorderHair)
                    ) {
                        Text(
                            text = stringResource(R.string.game_tilt_chip, state.tilt),
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .semantics { liveRegion = LiveRegionMode.Polite },
                            style = MaterialTheme.typography.labelSmall,
                            color = tiltColor(state.tilt)
                        )
                    }
                } else {
                    TiltRail(
                        tilt = state.tilt,
                        description = stringResource(R.string.cd_tilt, state.tilt),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 14.dp)
                    )
                }

                if (state.bannerVisible) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = LocalAppVisuals.current.safeAccent.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, LocalAppVisuals.current.safeAccent)
                    ) {
                        Text(
                            text = stringResource(R.string.game_banner, GameConfig.SHARDS_PER_WAVE),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = LocalAppVisuals.current.safeAccent
                        )
                    }
                }

                if (state.wave <= 2 && state.phase == GamePhase.BUILD) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 56.dp, start = 24.dp, end = 42.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.game_hint_cards),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(R.string.game_hint_floors),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextTertiary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            ControlBar(
                state = state,
                onSelect = onSelect,
                onDrop = onDrop,
                onKeystone = onKeystone
            )
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState, onRetreat: () -> Unit) {
    val visuals = LocalAppVisuals.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
        color = SurfaceBase.copy(alpha = 0.86f),
        border = BorderStroke(visuals.hairline, BorderHair)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .width(64.dp)
                    .height(58.dp)
                    .clickable(onClick = onRetreat),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_pause),
                    contentDescription = stringResource(R.string.cd_retreat),
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = stringResource(R.string.cta_retreat),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.game_wave, state.wave),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = stageName(state.stage),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.game_height, state.height),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.game_mortar, state.mortar),
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.labelSmall,
                    color = Secondary
                )
            }
        }
    }
}

@Composable
private fun SpireArea(
    state: GameUiState,
    onReinforce: (Int) -> Unit,
    onIntercept: (Int) -> Unit
) {
    val visuals = LocalAppVisuals.current
    val density = LocalDensity.current
    val targetAngle = if (state.collapsing) 32f else (state.tilt / 100f) * 7f
    val angle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = if (state.collapsing) tween(GameConfig.COLLAPSE_MS.toInt())
        else spring(dampingRatio = 0.8f, stiffness = 180f),
        label = "spireAngle"
    )
    val fade by animateFloatAsState(
        targetValue = if (state.collapsing) 0.1f else 1f,
        animationSpec = tween(GameConfig.COLLAPSE_MS.toInt()),
        label = "spireFade"
    )
    val shake = if (state.shakeTicks > 0 && !visuals.reducedMotion) {
        if (state.shakeTicks % 4 < 2) 6f else -6f
    } else {
        0f
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val areaWidthPx = with(density) { maxWidth.toPx() }
        val areaHeightPx = with(density) { maxHeight.toPx() }
        val floorPx = with(density) { FloorHeight.toPx() }
        val raiderPx = with(density) { RaiderSize.toPx() }
        val basePadPx = with(density) { 18.dp.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val base = size.height - basePadPx
            val shift = (state.tilt - 50) * 0.4f
            listOf(0.14f to 0.34f, 0.46f to 0.46f, 0.80f to 0.30f).forEach { pair ->
                val cx = size.width * pair.first + shift
                val halfWidth = size.width * 0.18f
                val path = Path().apply {
                    moveTo(cx - halfWidth, base)
                    lineTo(cx, base - size.height * pair.second)
                    lineTo(cx + halfWidth, base)
                    close()
                }
                drawPath(path = path, color = SurfaceSilhouette.copy(alpha = 0.22f))
            }
            drawRect(
                color = SurfaceSilhouette.copy(alpha = 0.30f),
                topLeft = Offset(0f, base),
                size = Size(size.width, (size.height - base).coerceAtLeast(0f))
            )
            var y = base
            while (y > size.height * 0.12f) {
                drawLine(
                    color = BorderCard.copy(alpha = 0.55f),
                    start = Offset(size.width / 2f, y),
                    end = Offset(size.width / 2f, y - 10f),
                    strokeWidth = 2f
                )
                y -= 20f
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .graphicsLayer {
                    rotationZ = angle
                    translationX = shake
                    translationY = if (state.collapsing) 60f else 0f
                    alpha = fade
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            val visible = state.floors.takeLast(GameConfig.MAX_VISIBLE_FLOORS)
            visible.reversed().forEach { floor ->
                val index = state.floors.indexOfFirst { it.id == floor.id }
                FloorView(
                    floor = floor,
                    width = (44 + floor.width * 34).dp,
                    height = FloorHeight,
                    description = stringResource(R.string.cd_floor, index + 1, floor.integrity),
                    reinforceable = state.canReinforce && floor.integrity < 100,
                    onReinforce = { onReinforce(floor.id) },
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .offset(x = floor.offsetX.dp)
                )
            }
        }

        state.raiders.forEach { raider ->
            if (raider.progress >= 0f) {
                val raiderDescription = stringResource(R.string.cd_raider, raider.lane + 1)
                val startX = if (raider.fromLeft) -raiderPx * 0.6f else areaWidthPx - raiderPx * 0.4f
                val endX = areaWidthPx / 2f - raiderPx / 2f
                val x = startX + (endX - startX) * raider.progress.coerceIn(0f, 1f)
                val y = (areaHeightPx - basePadPx - (raider.lane + 1) * floorPx - raiderPx * 0.4f)
                    .coerceIn(0f, (areaHeightPx - raiderPx).coerceAtLeast(0f))
                Box(
                    modifier = Modifier
                        .size(RaiderSize)
                        .offset { IntOffset(x.toInt(), y.toInt()) }
                        .clickable { onIntercept(raider.id) }
                        .semantics { contentDescription = raiderDescription }
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(RaiderSize)
                            .height(3.dp)
                            .background(Ember.copy(alpha = 0.8f))
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlBar(
    state: GameUiState,
    onSelect: (Int) -> Unit,
    onDrop: () -> Unit,
    onKeystone: () -> Unit
) {
    val visuals = LocalAppVisuals.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(bottom = 18.dp),
        shape = RoundedCornerShape(22.dp),
        color = SurfaceAlt.copy(alpha = 0.95f),
        border = BorderStroke(visuals.cardBorder, BorderCard),
        shadowElevation = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
            ) {
                drawRect(color = SurfaceElevated, size = size)
                drawRect(
                    color = Primary,
                    size = Size(size.width * state.phaseFraction.coerceIn(0f, 1f), size.height)
                )
            }

            if (state.phase == GamePhase.BUILD) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.trio.forEachIndexed { index, card ->
                        BlockCardView(
                            card = card,
                            familyLabel = familyName(card.family),
                            flawLabel = flawName(card.flaw),
                            statLine = statLineFor(card),
                            description = stringResource(
                                R.string.cd_block_card,
                                familyName(card.family),
                                card.tiltDelta,
                                flawName(card.flaw)
                            ),
                            selected = index == state.selected,
                            detailed = state.detailedCards,
                            onSelect = { onSelect(index) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SecondaryButton(
                        label = if (state.keystoneBanked != null) {
                            stringResource(R.string.cta_deploy)
                        } else {
                            stringResource(R.string.cta_keystone)
                        },
                        onClick = onKeystone,
                        enabled = state.keystoneBanked != null || state.keystoneCharges > 0,
                        accent = if (state.keystoneBanked != null) visuals.safeAccent else BorderCard,
                        modifier = Modifier.width(118.dp)
                    )
                    PrimaryButton(
                        label = stringResource(R.string.cta_drop),
                        onClick = onDrop,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(
                            R.string.game_raiders,
                            state.raidersLeft,
                            state.raidersTotal
                        ),
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = stringResource(R.string.game_hint_raiders),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
                PrimaryButton(
                    label = stringResource(R.string.cta_under_attack),
                    onClick = onDrop,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    gradient = listOf(Ember, EmberDeep),
                    glow = Color.Transparent
                )
            }
        }
    }
}

@Composable
private fun statLineFor(card: BlockCard): String = stringResource(
    R.string.card_stat_line,
    card.width,
    card.armor,
    card.tiltDelta
)
