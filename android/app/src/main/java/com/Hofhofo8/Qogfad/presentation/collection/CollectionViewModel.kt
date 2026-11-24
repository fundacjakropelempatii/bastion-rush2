package com.Hofhofo8.Qogfad.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Hofhofo8.Qogfad.data.sample.SampleData
import com.Hofhofo8.Qogfad.domain.model.Relic
import com.Hofhofo8.Qogfad.domain.model.RelicId
import com.Hofhofo8.Qogfad.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class RelicEntry(
    val relic: Relic,
    val unlocked: Boolean,
    val drops: Int
)

data class CollectionUiState(
    val entries: List<RelicEntry> = emptyList(),
    val detail: RelicId? = null
) {
    val unlockedCount: Int get() = entries.count { it.unlocked }
    val total: Int get() = entries.size
    val empty: Boolean get() = entries.isNotEmpty() && unlockedCount == 0
}

class CollectionViewModel(progressRepository: ProgressRepository) : ViewModel() {

    private val detail = MutableStateFlow<RelicId?>(null)

    val state: StateFlow<CollectionUiState> =
        combine(progressRepository.progress, detail) { progress, selected ->
            CollectionUiState(
                entries = SampleData.relics.map { relic ->
                    RelicEntry(
                        relic = relic,
                        unlocked = relic.id in progress.unlockedRelics,
                        drops = progress.familyDrops[relic.family] ?: 0
                    )
                },
                detail = selected
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, CollectionUiState())

    fun open(entry: RelicEntry) {
        if (entry.unlocked) detail.value = entry.relic.id
    }

    fun dismiss() {
        detail.value = null
    }
}
