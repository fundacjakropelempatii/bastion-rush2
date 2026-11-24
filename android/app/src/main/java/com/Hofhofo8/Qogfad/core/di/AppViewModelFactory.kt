package com.Hofhofo8.Qogfad.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import com.Hofhofo8.Qogfad.domain.repository.SettingsRepository
import com.Hofhofo8.Qogfad.domain.usecase.ApplyBlockUseCase
import com.Hofhofo8.Qogfad.domain.usecase.EvaluateRunUseCase
import com.Hofhofo8.Qogfad.domain.usecase.GenerateTrioUseCase
import com.Hofhofo8.Qogfad.domain.usecase.ResolveRaiderUseCase
import com.Hofhofo8.Qogfad.domain.usecase.UnlockStagesUseCase
import com.Hofhofo8.Qogfad.domain.usecase.UpdateMissionsUseCase
import com.Hofhofo8.Qogfad.presentation.app.AppViewModel
import com.Hofhofo8.Qogfad.presentation.collection.CollectionViewModel
import com.Hofhofo8.Qogfad.presentation.game.GameViewModel
import com.Hofhofo8.Qogfad.presentation.gameover.GameOverViewModel
import com.Hofhofo8.Qogfad.presentation.levelmap.LevelMapViewModel
import com.Hofhofo8.Qogfad.presentation.menu.MenuViewModel
import com.Hofhofo8.Qogfad.presentation.missions.MissionsViewModel
import com.Hofhofo8.Qogfad.presentation.rewards.RewardsViewModel
import com.Hofhofo8.Qogfad.presentation.settings.SettingsViewModel
import com.Hofhofo8.Qogfad.presentation.splash.SplashViewModel
import com.Hofhofo8.Qogfad.presentation.tutorial.TutorialViewModel

class AppViewModelFactory(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val generateTrio: GenerateTrioUseCase,
    private val applyBlock: ApplyBlockUseCase,
    private val resolveRaider: ResolveRaiderUseCase,
    private val updateMissions: UpdateMissionsUseCase,
    private val unlockStages: UnlockStagesUseCase,
    private val evaluateRun: EvaluateRunUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel(settingsRepository)

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(progressRepository, unlockStages, updateMissions)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    progressRepository,
                    settingsRepository,
                    generateTrio,
                    applyBlock,
                    resolveRaider,
                    evaluateRun
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(progressRepository)

            modelClass.isAssignableFrom(LevelMapViewModel::class.java) ->
                LevelMapViewModel(progressRepository, unlockStages)

            modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
                RewardsViewModel(progressRepository)

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(progressRepository)

            modelClass.isAssignableFrom(MissionsViewModel::class.java) ->
                MissionsViewModel(progressRepository, updateMissions)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(settingsRepository, progressRepository)

            modelClass.isAssignableFrom(TutorialViewModel::class.java) ->
                TutorialViewModel()

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
