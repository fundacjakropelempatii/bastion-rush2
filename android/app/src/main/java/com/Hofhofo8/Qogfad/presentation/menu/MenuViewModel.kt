package com.Hofhofo8.Qogfad.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.usecase.UnlockStagesUseCase
import com.Hofhofo8.Qogfad.domain.usecase.UpdateMissionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    progressRepository: ProgressRepository,
    private val unlockStages: UnlockStagesUseCase,
    private val updateMissions: UpdateMissionsUseCase
) : ViewModel() {

    val state: StateFlow<MenuUiState> = progressRepository.progress
        .map { progress ->
            val stage = SampleData.stageOf(progress.activeStage)
            MenuUiState(
                bestHeight = progress.bestHeight,
                wavesCleared = progress.totalWaves,
                bestScore = progress.bestScore,
                shards = progress.shards,
                relicsUnlocked = progress.unlockedRelics.size,
                stagesUnlocked = unlockStages(progress).size,
                missionsReady = updateMissions(progress).count { it.ready },
                rewardsReady = SampleData.chests.any {
                    it.id !in progress.claimedChests && progress.shards >= it.cost
                },
                activeStage = progress.activeStage,
                activeModifier = stage.modifier
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MenuUiState())
}
