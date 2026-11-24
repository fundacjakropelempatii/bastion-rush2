package com.Hofhofo8.Qogfad.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.domain.model.MissionId
import com.Hofhofo8.Qogfad.domain.model.MissionState
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.usecase.UpdateMissionsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MissionsUiState(
    val missions: List<MissionState> = emptyList()
) {
    val readyCount: Int get() = missions.count { it.ready }
    val allClaimed: Boolean get() = missions.isNotEmpty() && missions.none { it.ready }
}

class MissionsViewModel(
    private val progressRepository: ProgressRepository,
    private val updateMissions: UpdateMissionsUseCase
) : ViewModel() {

    val state: StateFlow<MissionsUiState> = progressRepository.progress
        .map { MissionsUiState(missions = updateMissions(it)) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MissionsUiState())

    fun claim(mission: MissionState) {
        if (!mission.ready) return
        val id: MissionId = mission.mission.id
        progressRepository.update { progress ->
            val claims = progress.missionClaims.toMutableMap()
            claims[id] = (claims[id] ?: 0) + 1
            progress.copy(
                shards = progress.shards + mission.mission.reward,
                missionClaims = claims
            )
        }
    }
}
