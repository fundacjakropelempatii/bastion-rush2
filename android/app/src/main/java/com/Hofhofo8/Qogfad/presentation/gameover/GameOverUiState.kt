package com.Hofhofo8.Qogfad.presentation.gameover

import com.Hofhofo8.Qogfad.domain.model.RunResult

data class GameOverUiState(
    val result: RunResult = RunResult(),
    val bestScore: Int = 0,
    val shardBalance: Int = 0,
    val relicsUnlocked: Int = 0
)
