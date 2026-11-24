package com.Hofhofo8.Qogfad.core.navigation

import com.Hofhofo8.Qogfad.domain.model.RunResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val runId: Int) : Screen
    data class GameOver(val result: RunResult) : Screen
    data object LevelMap : Screen
    data object Missions : Screen
    data object Rewards : Screen
    data object Collection : Screen
    data object Tutorial : Screen
    data object Settings : Screen
}
