package com.Hofhofo8.Qogfad.domain.model

enum class StageId { STONEWATCH, FROSTFALL, EMBERHOLD, DUSKLIGHT }

enum class StageModifier { NONE, FROST_DRIFT, EMBER_RUSH, DUSK_TRIAL }

data class Stage(
    val id: StageId,
    val modifier: StageModifier,
    val crestFamily: BlockFamily,
    val startMortarBonus: Int,
    val extraRaiders: Int,
    val tiltDriftPerWave: Int,
    val armorMultiplier: Int,
    val keystoneCharges: Int
)
