package com.Hofhofo8.Qogfad.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.config.GameConfig
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.ConfirmDialog
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.SecondaryButton

@Composable
fun SettingsPanel(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onNumericTilt = { settingsViewModel.setNumericTilt(it) },
        onDetailedCards = { settingsViewModel.setDetailedCards(it) },
        onHighContrast = { settingsViewModel.setHighContrast(it) },
        onReducedMotion = { settingsViewModel.setReducedMotion(it) },
        onPace = { settingsViewModel.setPace(it) },
        onAskReset = { settingsViewModel.askReset() },
        onCancelReset = { settingsViewModel.cancelReset() },
        onConfirmReset = { settingsViewModel.confirmReset() },
        onMenu = onMenu
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onNumericTilt: (Boolean) -> Unit,
    onDetailedCards: (Boolean) -> Unit,
    onHighContrast: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onPace: (Long) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onMenu: () -> Unit
) {
    val visuals = LocalAppVisuals.current
    val settings = state.settings

    ScreenScaffold(
        overlay = listOf(
            BgBase.copy(alpha = 0.82f),
            BgSink.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.settings_title),
                subtitle = stringResource(R.string.settings_sub),
                onMenu = onMenu
            )

            SettingRow(label = stringResource(R.string.setting_tilt_readout)) {
                Segmented(
                    options = listOf(
                        stringResource(R.string.option_bar) to false,
                        stringResource(R.string.option_numeric) to true
                    ),
                    selected = settings.numericTilt,
                    onSelect = onNumericTilt
                )
            }

            SettingRow(label = stringResource(R.string.setting_card_detail)) {
                Segmented(
                    options = listOf(
                        stringResource(R.string.option_compact) to false,
                        stringResource(R.string.option_detailed) to true
                    ),
                    selected = settings.detailedCards,
                    onSelect = onDetailedCards
                )
            }

            val contrastDescription = stringResource(
                if (settings.highContrast) R.string.state_on else R.string.state_off
            )
            SettingRow(label = stringResource(R.string.setting_high_contrast)) {
                Switch(
                    checked = settings.highContrast,
                    onCheckedChange = onHighContrast,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Primary,
                        uncheckedTrackColor = SurfaceElevated
                    ),
                    modifier = Modifier.semantics { stateDescription = contrastDescription }
                )
            }

            SettingRow(label = stringResource(R.string.setting_reduced_motion)) {
                Switch(
                    checked = settings.reducedMotion,
                    onCheckedChange = onReducedMotion,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Primary,
                        uncheckedTrackColor = SurfaceElevated
                    )
                )
            }

            SettingRow(label = stringResource(R.string.setting_pace)) {
                Segmented(
                    options = listOf(
                        stringResource(R.string.option_relaxed) to GameConfig.PACE_RELAXED_MS,
                        stringResource(R.string.option_standard) to GameConfig.PACE_STANDARD_MS,
                        stringResource(R.string.option_swift) to GameConfig.PACE_SWIFT_MS
                    ),
                    selected = settings.paceMs,
                    onSelect = onPace
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            SecondaryButton(
                label = stringResource(R.string.cta_reset),
                onClick = onAskReset,
                accent = visuals.dangerAccent,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.settings_reset_hint, state.bestScore, state.shards),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }
    }

    if (state.confirmReset) {
        ConfirmDialog(
            title = stringResource(R.string.reset_title),
            body = stringResource(R.string.reset_body),
            confirmLabel = stringResource(R.string.cta_reset_confirm),
            dismissLabel = stringResource(R.string.cta_cancel),
            onConfirm = onConfirmReset,
            onDismiss = onCancelReset
        )
    }
}

@Composable
private fun SettingRow(label: String, control: @Composable () -> Unit) {
    GlassCard(modifier = Modifier.fillMaxWidth(), radius = 16.dp, elevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                modifier = Modifier.width(132.dp),
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary
            )
            control()
        }
    }
}

@Composable
private fun <T> Segmented(
    options: List<Pair<String, T>>,
    selected: T,
    onSelect: (T) -> Unit
) {
    val visuals = LocalAppVisuals.current
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { option ->
            val active = option.second == selected
            Surface(
                onClick = { onSelect(option.second) },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (active) Primary.copy(alpha = 0.26f) else SurfaceElevated,
                border = BorderStroke(
                    visuals.cardBorder,
                    if (active) Primary else BorderHair
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.first,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (active) TextPrimary else TextSecondary
                    )
                }
            }
        }
    }
}
