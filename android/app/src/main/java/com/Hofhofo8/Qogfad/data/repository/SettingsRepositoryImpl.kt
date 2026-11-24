package com.Hofhofo8.Qogfad.data.repository

import com.Hofhofo8.Qogfad.core.config.GameConfig
import com.Hofhofo8.Qogfad.data.local.PrefKeys
import com.Hofhofo8.Qogfad.data.local.PreferencesStore
import com.Hofhofo8.Qogfad.domain.model.GameSettings
import com.Hofhofo8.Qogfad.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val store: PreferencesStore) : SettingsRepository {

    private val state = MutableStateFlow(read())

    override val settings: StateFlow<GameSettings> = state.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        val next = transform(state.value)
        state.value = next
        persist(next)
    }

    override fun reset() {
        val fresh = GameSettings(paceMs = GameConfig.PACE_STANDARD_MS)
        state.value = fresh
        persist(fresh)
    }

    private fun read(): GameSettings = GameSettings(
        numericTilt = store.bool(PrefKeys.NUMERIC_TILT, false),
        detailedCards = store.bool(PrefKeys.DETAILED_CARDS, true),
        highContrast = store.bool(PrefKeys.HIGH_CONTRAST, false),
        reducedMotion = store.bool(PrefKeys.REDUCED_MOTION, false),
        paceMs = store.long(PrefKeys.PACE_MS, GameConfig.PACE_STANDARD_MS)
    )

    private fun persist(value: GameSettings) {
        store.write {
            putBoolean(PrefKeys.NUMERIC_TILT, value.numericTilt)
            putBoolean(PrefKeys.DETAILED_CARDS, value.detailedCards)
            putBoolean(PrefKeys.HIGH_CONTRAST, value.highContrast)
            putBoolean(PrefKeys.REDUCED_MOTION, value.reducedMotion)
            putLong(PrefKeys.PACE_MS, value.paceMs)
        }
    }
}
