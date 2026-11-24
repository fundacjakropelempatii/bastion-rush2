package com.Hofhofo8.Qogfad.presentation.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.Hofhofo8.Qogfad.core.ui.ProgressRow
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.domain.model.MissionState
import com.Hofhofo8.Qogfad.presentation.common.missionTitle

@Composable
fun MissionsPanel(onMenu: () -> Unit) {
    val missionsViewModel: MissionsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by missionsViewModel.state.collectAsStateWithLifecycle()
    MissionsContent(state = state, onClaim = { missionsViewModel.claim(it) }, onMenu = onMenu)
}

@Composable
private fun MissionsContent(
    state: MissionsUiState,
    onClaim: (MissionState) -> Unit,
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            ScreenHeader(
                title = stringResource(R.string.missions_title),
                subtitle = stringResource(R.string.missions_ready, state.readyCount),
                onMenu = onMenu
            )

            if (state.allClaimed) {
                Text(
                    text = stringResource(R.string.missions_all_claimed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.missions, key = { it.mission.id.name }) { mission ->
                    ProgressRow(
                        title = missionTitle(mission.mission.id),
                        counter = stringResource(
                            R.string.missions_counter,
                            mission.current.coerceAtMost(mission.target),
                            mission.target
                        ),
                        rewardLabel = stringResource(
                            R.string.missions_reward,
                            mission.mission.reward
                        ),
                        fraction = mission.fraction,
                        description = stringResource(
                            R.string.cd_mission,
                            mission.current,
                            mission.target
                        ),
                        actionLabel = stringResource(R.string.cta_claim),
                        actionEnabled = mission.ready,
                        done = false,
                        doneLabel = stringResource(R.string.state_done),
                        rewardIcon = painterResource(R.drawable.sprite_5),
                        onAction = { onClaim(mission) }
                    )
                }
            }
        }
    }
}
