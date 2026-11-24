package com.Hofhofo8.Qogfad.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.ChestId
import com.Hofhofo8.Qogfad.domain.model.Flaw
import com.Hofhofo8.Qogfad.domain.model.MissionId
import com.Hofhofo8.Qogfad.domain.model.RelicId
import com.Hofhofo8.Qogfad.domain.model.RunOutcome
import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.model.StageModifier

@Composable
fun stageName(id: StageId): String = stringResource(
    when (id) {
        StageId.STONEWATCH -> R.string.stage_stonewatch
        StageId.FROSTFALL -> R.string.stage_frostfall
        StageId.EMBERHOLD -> R.string.stage_emberhold
        StageId.DUSKLIGHT -> R.string.stage_dusklight
    }
)

@Composable
fun stageUnlockHint(id: StageId): String = stringResource(
    when (id) {
        StageId.STONEWATCH -> R.string.unlock_stonewatch
        StageId.FROSTFALL -> R.string.unlock_frostfall
        StageId.EMBERHOLD -> R.string.unlock_emberhold
        StageId.DUSKLIGHT -> R.string.unlock_dusklight
    }
)

@Composable
fun stageModifierName(modifier: StageModifier): String = stringResource(
    when (modifier) {
        StageModifier.NONE -> R.string.mod_none
        StageModifier.FROST_DRIFT -> R.string.mod_frost
        StageModifier.EMBER_RUSH -> R.string.mod_ember
        StageModifier.DUSK_TRIAL -> R.string.mod_dusk
    }
)

@Composable
fun familyName(family: BlockFamily): String = stringResource(
    when (family) {
        BlockFamily.GRANITE -> R.string.family_granite
        BlockFamily.TIMBER -> R.string.family_timber
        BlockFamily.KEYSTONE -> R.string.family_keystone
    }
)

@Composable
fun flawName(flaw: Flaw): String = stringResource(
    when (flaw) {
        Flaw.CRACKED -> R.string.flaw_cracked
        Flaw.HEAVY -> R.string.flaw_heavy
        Flaw.NARROW -> R.string.flaw_narrow
        Flaw.BRITTLE -> R.string.flaw_brittle
        Flaw.TRUE_CUT -> R.string.flaw_true
    }
)

@Composable
fun missionTitle(id: MissionId): String = stringResource(
    when (id) {
        MissionId.CLEAR_WAVES -> R.string.mission_clear_waves
        MissionId.INTERCEPTS -> R.string.mission_intercepts
        MissionId.REACH_HEIGHT -> R.string.mission_reach_height
        MissionId.REINFORCE_FLOORS -> R.string.mission_reinforce
        MissionId.TRUE_BLOCKS -> R.string.mission_true_blocks
        MissionId.FROSTFALL_RUN -> R.string.mission_frostfall
    }
)

@Composable
fun relicName(id: RelicId): String = stringResource(
    when (id) {
        RelicId.QUARRY_SLAB -> R.string.relic_quarry_slab
        RelicId.KEYSTONE_GRANITE -> R.string.relic_keystone_granite
        RelicId.BUTTRESS -> R.string.relic_buttress
        RelicId.WATCHSTONE -> R.string.relic_watchstone
        RelicId.BRACE -> R.string.relic_brace
        RelicId.SCAFFOLD_BEAM -> R.string.relic_scaffold_beam
        RelicId.LATTICE -> R.string.relic_lattice
        RelicId.OAK_CORE -> R.string.relic_oak_core
        RelicId.CRACKED_CROWN -> R.string.relic_cracked_crown
        RelicId.PLUMB_ANCHOR -> R.string.relic_plumb_anchor
        RelicId.VAULT_CAP -> R.string.relic_vault_cap
        RelicId.EMBER_SEAL -> R.string.relic_ember_seal
    }
)

@Composable
fun relicTrait(id: RelicId): String = stringResource(
    when (id) {
        RelicId.QUARRY_SLAB -> R.string.trait_quarry_slab
        RelicId.KEYSTONE_GRANITE -> R.string.trait_keystone_granite
        RelicId.BUTTRESS -> R.string.trait_buttress
        RelicId.WATCHSTONE -> R.string.trait_watchstone
        RelicId.BRACE -> R.string.trait_brace
        RelicId.SCAFFOLD_BEAM -> R.string.trait_scaffold_beam
        RelicId.LATTICE -> R.string.trait_lattice
        RelicId.OAK_CORE -> R.string.trait_oak_core
        RelicId.CRACKED_CROWN -> R.string.trait_cracked_crown
        RelicId.PLUMB_ANCHOR -> R.string.trait_plumb_anchor
        RelicId.VAULT_CAP -> R.string.trait_vault_cap
        RelicId.EMBER_SEAL -> R.string.trait_ember_seal
    }
)

@Composable
fun chestName(id: ChestId): String = stringResource(
    when (id) {
        ChestId.BRONZE -> R.string.chest_bronze
        ChestId.SILVER -> R.string.chest_silver
        ChestId.GOLD -> R.string.chest_gold
    }
)

@Composable
fun outcomeHeadline(outcome: RunOutcome): String = stringResource(
    when (outcome) {
        RunOutcome.TILT_COLLAPSE -> R.string.result_tilt_collapse
        RunOutcome.HEIGHT_COLLAPSE -> R.string.result_height_collapse
        RunOutcome.RETREAT -> R.string.result_retreat
    }
)
