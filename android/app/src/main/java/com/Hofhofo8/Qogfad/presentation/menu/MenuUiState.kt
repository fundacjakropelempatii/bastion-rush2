package com.Hofhofo8.Qogfad.presentation.menu

import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.model.StageModifier

data class MenuUiState(
    val bestHeight: Int = 0,
    val wavesCleared: Int = 0,
    val bestScore: Int = 0,
    val shards: Int = 0,
    val relicsUnlocked: Int = 0,
    val stagesUnlocked: Int = 1,
    val missionsReady: Int = 0,
    val rewardsReady: Boolean = false,
    val activeStage: StageId = StageId.STONEWATCH,
    val activeModifier: StageModifier = StageModifier.NONE
) {
    val statsVisible: Boolean get() = bestHeight > 0 && wavesCleared > 0
}
