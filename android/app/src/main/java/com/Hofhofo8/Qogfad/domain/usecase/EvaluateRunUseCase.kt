package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.Progress
import com.Hofhofo8.Qogfad.domain.model.Relic
import com.Hofhofo8.Qogfad.domain.model.RunOutcome
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.domain.model.StageId

data class RunTally(
    val outcome: RunOutcome,
    val stage: StageId,
    val score: Int,
    val waves: Int,
    val height: Int,
    val intercepts: Int,
    val reinforces: Int,
    val trueBlocks: Int,
    val drops: Map<BlockFamily, Int>
)

data class RunEvaluation(
    val result: RunResult,
    val progress: Progress
)

class EvaluateRunUseCase(
    private val relics: List<Relic>,
    private val missions: UpdateMissionsUseCase,
    private val shardsPerWave: Int,
    private val recordBonus: Int
) {

    operator fun invoke(tally: RunTally, progress: Progress): RunEvaluation {
        val newRecord = tally.score > progress.bestScore && tally.score > 0
        val shards = tally.waves * shardsPerWave +
            tally.intercepts / 2 +
            if (newRecord) recordBonus else 0

        val drops = progress.familyDrops.toMutableMap()
        tally.drops.forEach { (family, count) ->
            drops[family] = (drops[family] ?: 0) + count
        }

        val unlockedRelics = progress.unlockedRelics.toMutableSet()
        relics.forEach { relic ->
            if ((drops[relic.family] ?: 0) >= relic.dropsRequired) unlockedRelics.add(relic.id)
        }

        val updated = progress.copy(
            bestScore = maxOf(progress.bestScore, tally.score),
            bestHeight = maxOf(progress.bestHeight, tally.height),
            totalWaves = progress.totalWaves + tally.waves,
            shards = progress.shards + shards,
            totalIntercepts = progress.totalIntercepts + tally.intercepts,
            totalReinforces = progress.totalReinforces + tally.reinforces,
            trueBlocks = progress.trueBlocks + tally.trueBlocks,
            frostfallRuns = progress.frostfallRuns + if (tally.stage == StageId.FROSTFALL) 1 else 0,
            bestWavesInRun = maxOf(progress.bestWavesInRun, tally.waves),
            familyDrops = drops,
            unlockedRelics = unlockedRelics
        )

        val ready = missions(updated).filter { it.ready }.take(3).map { it.mission.id }

        return RunEvaluation(
            result = RunResult(
                outcome = tally.outcome,
                stage = tally.stage,
                score = tally.score,
                waves = tally.waves,
                height = tally.height,
                intercepts = tally.intercepts,
                shards = shards,
                newRecord = newRecord,
                completed = ready
            ),
            progress = updated
        )
    }
}
