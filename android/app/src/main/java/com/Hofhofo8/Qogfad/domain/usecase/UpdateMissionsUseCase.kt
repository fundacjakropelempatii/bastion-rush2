package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.Mission
import com.Hofhofo8.Qogfad.domain.model.MissionMetric
import com.Hofhofo8.Qogfad.domain.model.MissionState
import com.Hofhofo8.Qogfad.domain.model.Progress

class UpdateMissionsUseCase(private val catalog: List<Mission>) {

    operator fun invoke(progress: Progress): List<MissionState> = catalog.map { mission ->
        val claims = progress.missionClaims[mission.id] ?: 0
        val target = mission.baseTarget * (claims + 1)
        MissionState(
            mission = mission,
            current = metricValue(mission.metric, progress),
            target = target,
            claims = claims
        )
    }

    private fun metricValue(metric: MissionMetric, progress: Progress): Int = when (metric) {
        MissionMetric.WAVES_IN_RUN -> progress.bestWavesInRun
        MissionMetric.INTERCEPTS_TOTAL -> progress.totalIntercepts
        MissionMetric.BEST_HEIGHT -> progress.bestHeight
        MissionMetric.REINFORCES_TOTAL -> progress.totalReinforces
        MissionMetric.TRUE_BLOCKS_TOTAL -> progress.trueBlocks
        MissionMetric.FROSTFALL_RUNS -> progress.frostfallRuns
    }
}
