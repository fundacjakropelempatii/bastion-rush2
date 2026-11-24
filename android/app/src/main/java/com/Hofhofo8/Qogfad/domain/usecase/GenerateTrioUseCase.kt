package com.Hofhofo8.Qogfad.domain.usecase

import com.Hofhofo8.Qogfad.domain.model.BlockCard
import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.Flaw
import kotlin.random.Random

class GenerateTrioUseCase {

    operator fun invoke(
        random: Random,
        currentTilt: Int,
        maxTilt: Int,
        familyBias: Map<BlockFamily, Int>,
        allBadPercent: Int
    ): List<BlockCard> {
        var attempt = 0
        while (attempt < MAX_ATTEMPTS) {
            attempt++
            val forceAllBad = random.nextInt(100) < allBadPercent
            val trio = List(3) { card(random, familyBias) }
            val shaped = if (forceAllBad) trio.map { harden(it) } else ensureStable(trio, random, familyBias)
            if (shaped.any { currentTilt + it.tiltDelta < maxTilt }) return shaped
        }
        return listOf(
            fallbackCard(BlockFamily.TIMBER),
            card(random, familyBias),
            card(random, familyBias)
        )
    }

    private fun ensureStable(
        trio: List<BlockCard>,
        random: Random,
        familyBias: Map<BlockFamily, Int>
    ): List<BlockCard> {
        if (trio.any { it.tiltDelta <= STABLE_DELTA }) return trio
        val slot = random.nextInt(trio.size)
        var stable = card(random, familyBias)
        var guard = 0
        while (stable.tiltDelta > STABLE_DELTA && guard < 24) {
            stable = card(random, familyBias)
            guard++
        }
        if (stable.tiltDelta > STABLE_DELTA) stable = fallbackCard(stable.family)
        return trio.mapIndexed { index, existing -> if (index == slot) stable else existing }
    }

    private fun harden(source: BlockCard): BlockCard =
        if (source.tiltDelta > STABLE_DELTA) source
        else source.copy(
            flaw = Flaw.HEAVY,
            weight = (source.weight + 1).coerceAtMost(3),
            tiltDelta = (source.tiltDelta + 9).coerceIn(MIN_DELTA, MAX_DELTA)
        )

    private fun card(random: Random, familyBias: Map<BlockFamily, Int>): BlockCard {
        val family = pickFamily(random, familyBias)
        val weight = 1 + random.nextInt(3)
        val armor = random.nextInt(4)
        var width = 1 + random.nextInt(3)
        val flaw = Flaw.entries[random.nextInt(Flaw.entries.size)]
        if (flaw == Flaw.NARROW) width = 1
        val finalArmor = if (flaw == Flaw.BRITTLE) 0 else armor
        val integrity = if (flaw == Flaw.CRACKED) 60 else 100
        var delta = weight * 4 - width * 3 + 2
        if (flaw == Flaw.HEAVY) delta += 5
        if (flaw == Flaw.TRUE_CUT) delta -= 4
        return BlockCard(
            family = family,
            weight = weight,
            armor = finalArmor,
            width = width,
            flaw = flaw,
            tiltDelta = delta.coerceIn(MIN_DELTA, MAX_DELTA),
            startIntegrity = integrity
        )
    }

    private fun fallbackCard(family: BlockFamily): BlockCard = BlockCard(
        family = family,
        weight = 1,
        armor = 1,
        width = 3,
        flaw = Flaw.TRUE_CUT,
        tiltDelta = -3,
        startIntegrity = 100
    )

    private fun pickFamily(random: Random, familyBias: Map<BlockFamily, Int>): BlockFamily {
        val families = BlockFamily.entries
        val weights = families.map { 100 + (familyBias[it] ?: 0) }
        val total = weights.sum()
        var roll = random.nextInt(total)
        families.forEachIndexed { index, family ->
            roll -= weights[index]
            if (roll < 0) return family
        }
        return families.first()
    }

    private companion object {
        const val MAX_ATTEMPTS = 20
        const val STABLE_DELTA = 4
        const val MIN_DELTA = -6
        const val MAX_DELTA = 18
    }
}
