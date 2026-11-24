package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.Floor

data class RaiderImpact(
    val floors: List<Floor>,
    val tiltAdded: Int,
    val floorLost: Boolean
)

class ResolveRaiderUseCase {

    operator fun invoke(
        floors: List<Floor>,
        targetFloorId: Int,
        wave: Int,
        armorMultiplier: Int,
        landedTilt: Int,
        floorLostTilt: Int
    ): RaiderImpact {
        val index = floors.indexOfFirst { it.id == targetFloorId }
        val fallback = if (index >= 0) index else floors.lastIndex
        if (fallback < 0) return RaiderImpact(floors, landedTilt, false)
        val target = floors[fallback]
        val damage = (18 + wave * 2 - target.armor * 4 * armorMultiplier).coerceAtLeast(6)
        val remaining = target.integrity - damage
        return if (remaining <= 0 && floors.size > 1) {
            RaiderImpact(
                floors = floors.filterIndexed { i, _ -> i != fallback },
                tiltAdded = landedTilt + floorLostTilt,
                floorLost = true
            )
        } else {
            val patched = floors.toMutableList()
            patched[fallback] = target.copy(integrity = remaining.coerceAtLeast(1))
            RaiderImpact(patched, landedTilt, false)
        }
    }
}
