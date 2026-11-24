package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.BlockCard
import com.Hofhofo8.Qogfad.domain.model.Floor
import kotlin.math.abs
import kotlin.random.Random

data class PlacementResult(
    val floors: List<Floor>,
    val tilt: Int
)

class ApplyBlockUseCase {

    operator fun invoke(
        floors: List<Floor>,
        card: BlockCard,
        tilt: Int,
        maxTilt: Int,
        nextFloorId: Int,
        random: Random
    ): PlacementResult {
        val spread = (4 - card.width).coerceAtLeast(1)
        val offset = (random.nextInt(37) - 18) * spread / 3
        val drift = abs(offset) / 6
        val placed = Floor(
            id = nextFloorId,
            family = card.family,
            width = card.width,
            offsetX = offset,
            armor = card.armor,
            integrity = card.startIntegrity
        )
        val newTilt = (tilt + card.tiltDelta + drift).coerceIn(0, maxTilt)
        return PlacementResult(floors + placed, newTilt)
    }
}
