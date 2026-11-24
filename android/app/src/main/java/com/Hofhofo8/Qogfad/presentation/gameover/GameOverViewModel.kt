package com.Hofhofo8.Qogfad.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GameOverViewModel(progressRepository: ProgressRepository) : ViewModel() {

    private val shown = MutableStateFlow(RunResult())

    val state: StateFlow<GameOverUiState> =
        combine(shown, progressRepository.progress) { result, progress ->
            GameOverUiState(
                result = result,
                bestScore = progress.bestScore,
                shardBalance = progress.shards,
                relicsUnlocked = progress.unlockedRelics.size
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, GameOverUiState())

    fun present(result: RunResult) {
        shown.value = result
    }
}
