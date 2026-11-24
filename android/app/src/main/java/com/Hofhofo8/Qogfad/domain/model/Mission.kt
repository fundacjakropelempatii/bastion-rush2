package com.Hofhofo8.Qogfad.domain.model

enum class MissionId {
    CLEAR_WAVES,
    INTERCEPTS,
    REACH_HEIGHT,
    REINFORCE_FLOORS,
    TRUE_BLOCKS,
    FROSTFALL_RUN
}

enum class MissionMetric {
    WAVES_IN_RUN,
    INTERCEPTS_TOTAL,
    BEST_HEIGHT,
    REINFORCES_TOTAL,
    TRUE_BLOCKS_TOTAL,
    FROSTFALL_RUNS
}

data class Mission(
    val id: MissionId,
    val metric: MissionMetric,
    val baseTarget: Int,
    val reward: Int
)

data class MissionState(
    val mission: Mission,
    val current: Int,
    val target: Int,
    val claims: Int
) {
    val ready: Boolean get() = current >= target
    val fraction: Float get() = if (target <= 0) 0f else (current.toFloat() / target).coerceIn(0f, 1f)
}
