package com.Hofhofo8.Qogfad.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.model.Chest
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ChestEntry(
    val chest: Chest,
    val claimed: Boolean,
    val affordable: Boolean,
    val fraction: Float
)

data class RewardsUiState(
    val shards: Int = 0,
    val entries: List<ChestEntry> = emptyList()
) {
    val nothingClaimable: Boolean get() = shards == 0 && entries.none { it.claimed }
}

class RewardsViewModel(private val progressRepository: ProgressRepository) : ViewModel() {

    val state: StateFlow<RewardsUiState> = progressRepository.progress
        .map { progress ->
            RewardsUiState(
                shards = progress.shards,
                entries = SampleData.chests.map { chest ->
                    ChestEntry(
                        chest = chest,
                        claimed = chest.id in progress.claimedChests,
                        affordable = progress.shards >= chest.cost,
                        fraction = (progress.shards.toFloat() / chest.cost).coerceIn(0f, 1f)
                    )
                }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, RewardsUiState())

    fun claim(chest: Chest) {
        progressRepository.update { progress ->
            if (chest.id in progress.claimedChests || progress.shards < chest.cost) {
                progress
            } else {
                progress.copy(
                    shards = progress.shards - chest.cost,
                    claimedChests = progress.claimedChests + chest.id,
                    unlockedRelics = progress.unlockedRelics + chest.relics
                )
            }
        }
    }
}
