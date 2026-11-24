package com.Hofhofo8.Qogfad.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.domain.model.GameSettings
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SettingsUiState(
    val settings: GameSettings = GameSettings(),
    val confirmReset: Boolean = false,
    val shards: Int = 0,
    val bestScore: Int = 0
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val confirm = MutableStateFlow(false)

    val state: StateFlow<SettingsUiState> = combine(
        settingsRepository.settings,
        progressRepository.progress,
        confirm
    ) { settings, progress, confirmReset ->
        SettingsUiState(
            settings = settings,
            confirmReset = confirmReset,
            shards = progress.shards,
            bestScore = progress.bestScore
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun setNumericTilt(value: Boolean) {
        settingsRepository.update { it.copy(numericTilt = value) }
    }

    fun setDetailedCards(value: Boolean) {
        settingsRepository.update { it.copy(detailedCards = value) }
    }

    fun setHighContrast(value: Boolean) {
        settingsRepository.update { it.copy(highContrast = value) }
    }

    fun setReducedMotion(value: Boolean) {
        settingsRepository.update { it.copy(reducedMotion = value) }
    }

    fun setPace(value: Long) {
        settingsRepository.update { it.copy(paceMs = value) }
    }

    fun askReset() {
        confirm.value = true
    }

    fun cancelReset() {
        confirm.value = false
    }

    fun confirmReset() {
        progressRepository.reset()
        confirm.value = false
    }
}
