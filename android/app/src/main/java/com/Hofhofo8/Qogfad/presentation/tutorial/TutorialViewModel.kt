package com.Hofhofo8.Qogfad.presentation.tutorial

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TutorialViewModel : ViewModel() {

    private val _step = MutableStateFlow(0)
    val step: StateFlow<Int> = _step.asStateFlow()

    fun next() {
        if (_step.value < LAST_STEP) _step.value = _step.value + 1
    }

    fun previous() {
        if (_step.value > 0) _step.value = _step.value - 1
    }

    fun restart() {
        _step.value = 0
    }

    companion object {
        const val STEP_COUNT = 4
        const val LAST_STEP = STEP_COUNT - 1
    }
}
