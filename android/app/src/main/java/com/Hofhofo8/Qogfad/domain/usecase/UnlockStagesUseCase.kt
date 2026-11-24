package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.Progress
import com.Hofhofo8.Qogfad.domain.model.StageId

class UnlockStagesUseCase(
    private val heightForFrostfall: Int,
    private val wavesForEmberhold: Int,
    private val missionsForDusklight: Int
) {

    operator fun invoke(progress: Progress): Set<StageId> {
        val unlocked = mutableSetOf(StageId.STONEWATCH)
        if (progress.bestHeight >= heightForFrostfall) unlocked.add(StageId.FROSTFALL)
        if (progress.totalWaves >= wavesForEmberhold) unlocked.add(StageId.EMBERHOLD)
        if (progress.missionClaims.values.sum() >= missionsForDusklight) unlocked.add(StageId.DUSKLIGHT)
        return unlocked
    }

    fun requirement(stage: StageId): Int = when (stage) {
        StageId.STONEWATCH -> 0
        StageId.FROSTFALL -> heightForFrostfall
        StageId.EMBERHOLD -> wavesForEmberhold
        StageId.DUSKLIGHT -> missionsForDusklight
    }
}
