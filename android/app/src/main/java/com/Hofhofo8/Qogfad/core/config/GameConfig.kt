package com.Hofhofo8.Qogfad.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val TICK_MS = 32L

    const val START_TILT = 12
    const val MAX_TILT = 100
    const val START_FLOORS = 3
    const val MIN_FLOORS = 3
    const val MAX_VISIBLE_FLOORS = 11

    const val START_MORTAR = 3
    const val MAX_MORTAR = 8
    const val REINFORCE_GAIN = 40
    const val REINFORCE_RELIEF = 3

    const val PACE_RELAXED_MS = 18000L
    const val PACE_STANDARD_MS = 12000L
    const val PACE_SWIFT_MS = 7000L

    const val RAIDER_MIN_CROSS_MS = 1200
    const val RAIDER_BASE_CROSS_MS = 3000
    const val RAIDER_CROSS_STEP_MS = 120
    const val RAIDER_LANDED_TILT = 3
    const val FLOOR_LOST_TILT = 14
    const val MAX_RAIDERS = 8

    const val INTERCEPT_SCORE = 10
    const val WAVE_SCORE = 50
    const val WAVE_HEIGHT_SCORE = 15
    const val SHARDS_PER_WAVE = 5
    const val SHARDS_RECORD = 20

    const val COLLAPSE_MS = 1100L
    const val BANNER_MS = 900L
    const val SHAKE_MS = 320L

    const val RELIC_BIAS_PERCENT = 6
    const val UNLOCK_FROSTFALL_HEIGHT = 8
    const val UNLOCK_EMBERHOLD_WAVES = 15
    const val UNLOCK_DUSKLIGHT_MISSIONS = 3
}
