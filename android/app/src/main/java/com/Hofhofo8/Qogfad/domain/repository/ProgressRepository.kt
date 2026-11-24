package com.Hofhofo8.Qogfad.domain.repository

import com.Hofhofo8.Qogfad.domain.model.Progress
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<Progress>
    fun update(transform: (Progress) -> Progress)
    fun reset()
}
