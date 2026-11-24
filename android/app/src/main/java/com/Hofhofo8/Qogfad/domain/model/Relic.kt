package com.Hofhofo8.Qogfad.domain.model

enum class RelicId {
    QUARRY_SLAB,
    KEYSTONE_GRANITE,
    BUTTRESS,
    WATCHSTONE,
    BRACE,
    SCAFFOLD_BEAM,
    LATTICE,
    OAK_CORE,
    CRACKED_CROWN,
    PLUMB_ANCHOR,
    VAULT_CAP,
    EMBER_SEAL
}

data class Relic(
    val id: RelicId,
    val family: BlockFamily,
    val dropsRequired: Int
)

enum class ChestId { BRONZE, SILVER, GOLD }

data class Chest(
    val id: ChestId,
    val cost: Int,
    val relics: List<RelicId>
)
