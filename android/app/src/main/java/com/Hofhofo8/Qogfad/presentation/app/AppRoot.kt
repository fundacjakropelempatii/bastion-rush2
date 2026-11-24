package com.Hofhofo8.Qogfad.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.navigation.Screen
import com.Hofhofo8.Qogfad.core.theme.AppVisuals
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.Ember
import com.Hofhofo8.Qogfad.core.theme.EmberBright
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Verdant
import com.Hofhofo8.Qogfad.core.theme.VerdantBright
import com.Hofhofo8.Qogfad.presentation.collection.CollectionPanel
import com.Hofhofo8.Qogfad.presentation.game.GameScreen
import com.Hofhofo8.Qogfad.presentation.gameover.GameOverScreen
import com.Hofhofo8.Qogfad.presentation.levelmap.LevelMapPanel
import com.Hofhofo8.Qogfad.presentation.menu.MenuScreen
import com.Hofhofo8.Qogfad.presentation.missions.MissionsPanel
import com.Hofhofo8.Qogfad.presentation.rewards.RewardsPanel
import com.Hofhofo8.Qogfad.presentation.settings.SettingsPanel
import com.Hofhofo8.Qogfad.presentation.splash.SplashScreen
import com.Hofhofo8.Qogfad.presentation.tutorial.TutorialPanel

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()
    val settings by appViewModel.settings.collectAsStateWithLifecycle()

    val visuals = remember(settings) {
        AppVisuals(
            hairline = if (settings.highContrast) 2.dp else 1.dp,
            cardBorder = if (settings.highContrast) 2.dp else 1.dp,
            surfaceAlpha = if (settings.highContrast) 1f else 0.92f,
            safeAccent = if (settings.highContrast) VerdantBright else Verdant,
            dangerAccent = if (settings.highContrast) EmberBright else Ember,
            reducedMotion = settings.reducedMotion
        )
    }

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    CompositionLocalProvider(LocalAppVisuals provides visuals) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgBase)
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = {
                    val duration = if (visuals.reducedMotion) 120 else 320
                    if (targetState is Screen.Game) {
                        (fadeIn(tween(duration)) + slideInVertically { it / 12 })
                            .togetherWith(fadeOut(tween(duration - 60)))
                            .using(SizeTransform(clip = false))
                    } else {
                        fadeIn(tween(duration))
                            .togetherWith(fadeOut(tween(duration - 60)))
                            .using(SizeTransform(clip = false))
                    }
                },
                label = "screenHost"
            ) { current ->
                when (current) {
                    Screen.Splash -> SplashScreen(
                        onFinished = { appViewModel.replace(Screen.Menu) }
                    )

                    Screen.Menu -> MenuScreen(
                        onPlay = { appViewModel.startRun() },
                        onOpen = { appViewModel.open(it) }
                    )

                    is Screen.Game -> GameScreen(
                        runId = current.runId,
                        onFinish = { appViewModel.finishRun(it) }
                    )

                    is Screen.GameOver -> GameOverScreen(
                        result = current.result,
                        onPlayAgain = { appViewModel.replayRun() },
                        onMenu = { appViewModel.home() }
                    )

                    Screen.LevelMap -> LevelMapPanel(onMenu = { appViewModel.home() })
                    Screen.Missions -> MissionsPanel(onMenu = { appViewModel.home() })
                    Screen.Rewards -> RewardsPanel(onMenu = { appViewModel.home() })
                    Screen.Collection -> CollectionPanel(onMenu = { appViewModel.home() })
                    Screen.Tutorial -> TutorialPanel(onMenu = { appViewModel.home() })
                    Screen.Settings -> SettingsPanel(onMenu = { appViewModel.home() })
                }
            }
        }
    }
}
