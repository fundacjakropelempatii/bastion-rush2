package com.Hofhofo8.Qogfad.data.sample

import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.Chest
import com.Hofhofo8.Qogfad.domain.model.ChestId
import com.Hofhofo8.Qogfad.domain.model.Mission
import com.Hofhofo8.Qogfad.domain.model.MissionId
import com.Hofhofo8.Qogfad.domain.model.MissionMetric
import com.Hofhofo8.Qogfad.domain.model.Relic
import com.Hofhofo8.Qogfad.domain.model.RelicId
import com.Hofhofo8.Qogfad.domain.model.Stage
import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.model.StageModifier

object SampleData {

    val stages: List<Stage> = listOf(
        Stage(
            id = StageId.STONEWATCH,
            modifier = StageModifier.NONE,
            crestFamily = BlockFamily.GRANITE,
            startMortarBonus = 0,
            extraRaiders = 0,
            tiltDriftPerWave = 0,
            armorMultiplier = 1,
            keystoneCharges = 1
        ),
        Stage(
            id = StageId.FROSTFALL,
            modifier = StageModifier.FROST_DRIFT,
            crestFamily = BlockFamily.TIMBER,
            startMortarBonus = 1,
            extraRaiders = 0,
            tiltDriftPerWave = 1,
            armorMultiplier = 1,
            keystoneCharges = 1
        ),
        Stage(
            id = StageId.EMBERHOLD,
            modifier = StageModifier.EMBER_RUSH,
            crestFamily = BlockFamily.KEYSTONE,
            startMortarBonus = 0,
            extraRaiders = 1,
            tiltDriftPerWave = 0,
            armorMultiplier = 2,
            keystoneCharges = 1
        ),
        Stage(
            id = StageId.DUSKLIGHT,
            modifier = StageModifier.DUSK_TRIAL,
            crestFamily = BlockFamily.KEYSTONE,
            startMortarBonus = 0,
            extraRaiders = 1,
            tiltDriftPerWave = 1,
            armorMultiplier = 1,
            keystoneCharges = 2
        )
    )

    val missions: List<Mission> = listOf(
        Mission(MissionId.CLEAR_WAVES, MissionMetric.WAVES_IN_RUN, 3, 30),
        Mission(MissionId.INTERCEPTS, MissionMetric.INTERCEPTS_TOTAL, 10, 25),
        Mission(MissionId.REACH_HEIGHT, MissionMetric.BEST_HEIGHT, 10, 40),
        Mission(MissionId.REINFORCE_FLOORS, MissionMetric.REINFORCES_TOTAL, 5, 20),
        Mission(MissionId.TRUE_BLOCKS, MissionMetric.TRUE_BLOCKS_TOTAL, 4, 35),
        Mission(MissionId.FROSTFALL_RUN, MissionMetric.FROSTFALL_RUNS, 1, 50)
    )

    val relics: List<Relic> = listOf(
        Relic(RelicId.QUARRY_SLAB, BlockFamily.GRANITE, 3),
        Relic(RelicId.KEYSTONE_GRANITE, BlockFamily.GRANITE, 6),
        Relic(RelicId.BUTTRESS, BlockFamily.GRANITE, 10),
        Relic(RelicId.WATCHSTONE, BlockFamily.GRANITE, 16),
        Relic(RelicId.BRACE, BlockFamily.TIMBER, 3),
        Relic(RelicId.SCAFFOLD_BEAM, BlockFamily.TIMBER, 6),
        Relic(RelicId.LATTICE, BlockFamily.TIMBER, 10),
        Relic(RelicId.OAK_CORE, BlockFamily.TIMBER, 16),
        Relic(RelicId.CRACKED_CROWN, BlockFamily.KEYSTONE, 3),
        Relic(RelicId.PLUMB_ANCHOR, BlockFamily.KEYSTONE, 6),
        Relic(RelicId.VAULT_CAP, BlockFamily.KEYSTONE, 10),
        Relic(RelicId.EMBER_SEAL, BlockFamily.KEYSTONE, 16)
    )

    val chests: List<Chest> = listOf(
        Chest(ChestId.BRONZE, 120, listOf(RelicId.QUARRY_SLAB, RelicId.BRACE)),
        Chest(ChestId.SILVER, 400, listOf(RelicId.CRACKED_CROWN, RelicId.SCAFFOLD_BEAM)),
        Chest(ChestId.GOLD, 900, listOf(RelicId.PLUMB_ANCHOR, RelicId.BUTTRESS))
    )

    fun stageOf(id: StageId): Stage = stages.firstOrNull { it.id == id } ?: stages.first()
}
