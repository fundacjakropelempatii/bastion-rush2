package com.Hofhofo8.Qogfad.domain.model

data class Floor(
    val id: Int,
    val family: BlockFamily,
    val width: Int,
    val offsetX: Int,
    val armor: Int,
    val integrity: Int
)

data class Raider(
    val id: Int,
    val fromLeft: Boolean,
    val targetFloorId: Int,
    val lane: Int,
    val progress: Float,
    val crossMs: Int
)
