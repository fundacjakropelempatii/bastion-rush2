package com.Hofhofo8.Qogfad.domain.repository

import com.Hofhofo8.Qogfad.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>
    fun update(transform: (GameSettings) -> GameSettings)
    fun reset()
}
