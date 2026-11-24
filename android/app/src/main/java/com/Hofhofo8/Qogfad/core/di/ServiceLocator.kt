package com.Hofhofo8.Qogfad.core.di

import android.content.Context
import com.Hofhofo8.Qogfad.core.config.GameConfig
import com.Hofhofo8.Qogfad.data.local.PreferencesStore
import com.Hofhofo8.Qogfad.data.repository.ProgressRepositoryImpl
import com.Hofhofo8.Qogfad.data.repository.SettingsRepositoryImpl
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.usecase.ApplyBlockUseCase
import com.Hofhofo8.Qogfad.domain.usecase.EvaluateRunUseCase
import com.Hofhofo8.Qogfad.domain.usecase.GenerateTrioUseCase
import com.Hofhofo8.Qogfad.domain.usecase.ResolveRaiderUseCase
import com.Hofhofo8.Qogfad.domain.usecase.UnlockStagesUseCase
import com.Hofhofo8.Qogfad.domain.usecase.UpdateMissionsUseCase

object ServiceLocator {

    private var cached: AppViewModelFactory? = null

    fun initialize(context: Context) {
        if (cached != null) return
        val store = PreferencesStore(context)
        val progress = ProgressRepositoryImpl(store)
        val settings = SettingsRepositoryImpl(store)
        val missions = UpdateMissionsUseCase(SampleData.missions)
        cached = AppViewModelFactory(
            progressRepository = progress,
            settingsRepository = settings,
            generateTrio = GenerateTrioUseCase(),
            applyBlock = ApplyBlockUseCase(),
            resolveRaider = ResolveRaiderUseCase(),
            updateMissions = missions,
            unlockStages = UnlockStagesUseCase(
                heightForFrostfall = GameConfig.UNLOCK_FROSTFALL_HEIGHT,
                wavesForEmberhold = GameConfig.UNLOCK_EMBERHOLD_WAVES,
                missionsForDusklight = GameConfig.UNLOCK_DUSKLIGHT_MISSIONS
            ),
            evaluateRun = EvaluateRunUseCase(
                relics = SampleData.relics,
                missions = missions,
                shardsPerWave = GameConfig.SHARDS_PER_WAVE,
                recordBonus = GameConfig.SHARDS_RECORD
            )
        )
    }

    val factory: AppViewModelFactory
        get() = checkNotNull(cached)
}
