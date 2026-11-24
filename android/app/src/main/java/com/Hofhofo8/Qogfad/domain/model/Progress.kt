package com.Hofhofo8.Qogfad.domain.model

data class Progress(
    val bestScore: Int = 0,
    val bestHeight: Int = 0,
    val totalWaves: Int = 0,
    val shards: Int = 0,
    val totalIntercepts: Int = 0,
    val totalReinforces: Int = 0,
    val trueBlocks: Int = 0,
    val frostfallRuns: Int = 0,
    val bestWavesInRun: Int = 0,
    val familyDrops: Map<BlockFamily, Int> = emptyMap(),
    val unlockedRelics: Set<RelicId> = emptySet(),
    val claimedChests: Set<ChestId> = emptySet(),
    val missionClaims: Map<MissionId, Int> = emptyMap(),
    val activeStage: StageId = StageId.STONEWATCH
)

data class GameSettings(
    val numericTilt: Boolean = false,
    val detailedCards: Boolean = true,
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val paceMs: Long = 12000L
)

enum class RunOutcome { TILT_COLLAPSE, HEIGHT_COLLAPSE, RETREAT }

data class RunResult(
    val outcome: RunOutcome = RunOutcome.RETREAT,
    val stage: StageId = StageId.STONEWATCH,
    val score: Int = 0,
    val waves: Int = 0,
    val height: Int = 0,
    val intercepts: Int = 0,
    val shards: Int = 0,
    val newRecord: Boolean = false,
    val completed: List<MissionId> = emptyList()
)
