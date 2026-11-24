package com.Hofhofo8.Qogfad.presentation.app

import androidx.lifecycle.ViewModel
import com.Hofhofo8.Qogfad.core.navigation.Screen
import com.Hofhofo8.Qogfad.domain.model.GameSettings
import com.Hofhofo8.Qogfad.domain.model.RunResult
import com.Hofhofo8.Qogfad.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(settingsRepository: SettingsRepository) : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    val settings: StateFlow<GameSettings> = settingsRepository.settings

    private var runCounter = 0

    fun open(target: Screen) {
        backStack.add(_screen.value)
        _screen.value = target
        syncBack()
    }

    fun replace(target: Screen) {
        _screen.value = target
        syncBack()
    }

    fun home() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBack()
    }

    fun startRun() {
        runCounter += 1
        open(Screen.Game(runCounter))
    }

    fun replayRun() {
        runCounter += 1
        replace(Screen.Game(runCounter))
    }

    fun finishRun(result: RunResult) {
        replace(Screen.GameOver(result))
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
