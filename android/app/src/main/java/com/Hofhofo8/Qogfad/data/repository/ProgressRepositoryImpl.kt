package com.Hofhofo8.Qogfad.data.repository

import com.Hofhofo8.Qogfad.data.local.PrefKeys
import com.Hofhofo8.Qogfad.data.local.PreferencesStore
import com.Hofhofo8.Qogfad.domain.model.BlockFamily
import com.Hofhofo8.Qogfad.domain.model.ChestId
import com.Hofhofo8.Qogfad.domain.model.MissionId
import com.Hofhofo8.Qogfad.domain.model.Progress
import com.Hofhofo8.Qogfad.domain.model.RelicId
import com.Hofhofo8.Qogfad.domain.model.StageId
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(private val store: PreferencesStore) : ProgressRepository {

    private val state = MutableStateFlow(read())

    override val progress: StateFlow<Progress> = state.asStateFlow()

    override fun update(transform: (Progress) -> Progress) {
        val next = transform(state.value)
        state.value = next
        persist(next)
    }

    override fun reset() {
        val fresh = Progress()
        state.value = fresh
        persist(fresh)
    }

    private fun read(): Progress = Progress(
        bestScore = store.int(PrefKeys.BEST_SCORE, 0),
        bestHeight = store.int(PrefKeys.BEST_HEIGHT, 0),
        totalWaves = store.int(PrefKeys.TOTAL_WAVES, 0),
        shards = store.int(PrefKeys.SHARDS, 0),
        totalIntercepts = store.int(PrefKeys.INTERCEPTS, 0),
        totalReinforces = store.int(PrefKeys.REINFORCES, 0),
        trueBlocks = store.int(PrefKeys.TRUE_BLOCKS, 0),
        frostfallRuns = store.int(PrefKeys.FROSTFALL_RUNS, 0),
        bestWavesInRun = store.int(PrefKeys.BEST_WAVES_RUN, 0),
        familyDrops = decodeCounts(store.text(PrefKeys.FAMILY_DROPS, "")) { key ->
            BlockFamily.entries.firstOrNull { it.name == key }
        },
        unlockedRelics = decodeSet(store.text(PrefKeys.RELICS, "")) { key ->
            RelicId.entries.firstOrNull { it.name == key }
        },
        claimedChests = decodeSet(store.text(PrefKeys.CHESTS, "")) { key ->
            ChestId.entries.firstOrNull { it.name == key }
        },
        missionClaims = decodeCounts(store.text(PrefKeys.MISSION_CLAIMS, "")) { key ->
            MissionId.entries.firstOrNull { it.name == key }
        },
        activeStage = StageId.entries.firstOrNull {
            it.name == store.text(PrefKeys.ACTIVE_STAGE, StageId.STONEWATCH.name)
        } ?: StageId.STONEWATCH
    )

    private fun persist(value: Progress) {
        store.write {
            putInt(PrefKeys.BEST_SCORE, value.bestScore)
            putInt(PrefKeys.BEST_HEIGHT, value.bestHeight)
            putInt(PrefKeys.TOTAL_WAVES, value.totalWaves)
            putInt(PrefKeys.SHARDS, value.shards)
            putInt(PrefKeys.INTERCEPTS, value.totalIntercepts)
            putInt(PrefKeys.REINFORCES, value.totalReinforces)
            putInt(PrefKeys.TRUE_BLOCKS, value.trueBlocks)
            putInt(PrefKeys.FROSTFALL_RUNS, value.frostfallRuns)
            putInt(PrefKeys.BEST_WAVES_RUN, value.bestWavesInRun)
            putString(PrefKeys.FAMILY_DROPS, encodeCounts(value.familyDrops) { it.name })
            putString(PrefKeys.RELICS, encodeSet(value.unlockedRelics) { it.name })
            putString(PrefKeys.CHESTS, encodeSet(value.claimedChests) { it.name })
            putString(PrefKeys.MISSION_CLAIMS, encodeCounts(value.missionClaims) { it.name })
            putString(PrefKeys.ACTIVE_STAGE, value.activeStage.name)
        }
    }

    private fun <K> encodeCounts(source: Map<K, Int>, name: (K) -> String): String =
        source.entries.joinToString(ENTRY_SEPARATOR) { name(it.key) + PAIR_SEPARATOR + it.value }

    private fun <K> decodeCounts(raw: String, parse: (String) -> K?): Map<K, Int> {
        if (raw.isBlank()) return emptyMap()
        val result = mutableMapOf<K, Int>()
        raw.split(ENTRY_SEPARATOR).forEach { chunk ->
            val parts = chunk.split(PAIR_SEPARATOR)
            if (parts.size == 2) {
                val key = parse(parts[0])
                val count = parts[1].toIntOrNull()
                if (key != null && count != null) result[key] = count
            }
        }
        return result
    }

    private fun <T> encodeSet(source: Set<T>, name: (T) -> String): String =
        source.joinToString(ENTRY_SEPARATOR) { name(it) }

    private fun <T> decodeSet(raw: String, parse: (String) -> T?): Set<T> {
        if (raw.isBlank()) return emptySet()
        return raw.split(ENTRY_SEPARATOR).mapNotNull { parse(it) }.toSet()
    }

    private companion object {
        const val ENTRY_SEPARATOR = ","
        const val PAIR_SEPARATOR = ":"
    }
}
