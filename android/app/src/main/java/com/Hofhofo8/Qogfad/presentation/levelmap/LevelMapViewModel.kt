package com.Hofhofo8.Qogfad.presentation.levelmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.model.Stage
import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.usecase.UnlockStagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class StageEntry(
    val stage: Stage,
    val unlocked: Boolean,
    val active: Boolean
)

data class LevelMapUiState(
    val entries: List<StageEntry> = emptyList(),
    val lockedDialog: StageId? = null
)

class LevelMapViewModel(
    private val progressRepository: ProgressRepository,
    private val unlockStages: UnlockStagesUseCase
) : ViewModel() {

    private val dialog = MutableStateFlow<StageId?>(null)

    val state: StateFlow<LevelMapUiState> =
        combine(progressRepository.progress, dialog) { progress, locked ->
            val unlocked = unlockStages(progress)
            LevelMapUiState(
                entries = SampleData.stages.map { stage ->
                    StageEntry(
                        stage = stage,
                        unlocked = stage.id in unlocked,
                        active = stage.id == progress.activeStage
                    )
                },
                lockedDialog = locked
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, LevelMapUiState())

    fun select(entry: StageEntry) {
        if (entry.unlocked) {
            progressRepository.update { it.copy(activeStage = entry.stage.id) }
        } else {
            dialog.value = entry.stage.id
        }
    }

    fun dismissDialog() {
        dialog.value = null
    }

    fun requirementFor(stage: StageId): Int = unlockStages.requirement(stage)
}
