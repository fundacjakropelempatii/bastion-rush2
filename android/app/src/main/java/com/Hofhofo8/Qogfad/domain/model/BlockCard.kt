package com.Hofhofo8.Qogfad.domain.model

enum class BlockFamily { GRANITE, TIMBER, KEYSTONE }

enum class Flaw { CRACKED, HEAVY, NARROW, BRITTLE, TRUE_CUT }

data class BlockCard(
    val family: BlockFamily,
    val weight: Int,
    val armor: Int,
    val width: Int,
    val flaw: Flaw,
    val tiltDelta: Int,
    val startIntegrity: Int
)
