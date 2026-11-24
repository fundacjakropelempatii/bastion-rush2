package com.Hofhofo8.Qogfad.presentation.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgBase
import com.Hofhofo8.Qogfad.core.theme.BgSink
import com.Hofhofo8.Qogfad.core.theme.BorderCard
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.ui.GlassCard
import com.Hofhofo8.Qogfad.core.ui.PrimaryButton
import com.Hofhofo8.Qogfad.core.ui.ScreenHeader
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold
import com.Hofhofo8.Qogfad.core.ui.SecondaryButton

@Composable
fun TutorialPanel(onMenu: () -> Unit) {
    val tutorialViewModel: TutorialViewModel = viewModel(factory = ServiceLocator.factory)
    val step by tutorialViewModel.step.collectAsStateWithLifecycle()

    TutorialContent(
        step = step,
        onNext = {
            if (step >= TutorialViewModel.LAST_STEP) {
                tutorialViewModel.restart()
                onMenu()
            } else {
                tutorialViewModel.next()
            }
        },
        onBack = { tutorialViewModel.previous() },
        onMenu = onMenu
    )
}

@Composable
private fun TutorialContent(
    step: Int,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onMenu: () -> Unit
) {
    val visuals = LocalAppVisuals.current
    val titles = listOf(
        R.string.tut_title_one,
        R.string.tut_title_two,
        R.string.tut_title_three,
        R.string.tut_title_four
    )
    val bodies = listOf(
        R.string.tut_body_one,
        R.string.tut_body_two,
        R.string.tut_body_three,
        R.string.tut_body_four
    )
    val art = listOf(
        R.drawable.sprite_1,
        R.drawable.sprite_3,
        R.drawable.sprite_4,
        R.drawable.logo
    )
    val safeStep = step.coerceIn(0, TutorialViewModel.LAST_STEP)

    ScreenScaffold(
        overlay = listOf(
            BgBase.copy(alpha = 0.86f),
            BgSink.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ScreenHeader(
                title = stringResource(R.string.tutorial_title),
                subtitle = stringResource(
                    R.string.tutorial_step,
                    safeStep + 1,
                    TutorialViewModel.STEP_COUNT
                ),
                onMenu = onMenu
            )

            GlassCard(modifier = Modifier.fillMaxWidth(), radius = 24.dp, elevation = 14.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = painterResource(art[safeStep]),
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = stringResource(titles[safeStep]),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(bodies[safeStep]),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(TutorialViewModel.STEP_COUNT) { index ->
                        Spacer(
                            modifier = Modifier
                                .size(if (index == safeStep) 10.dp else 6.dp)
                                .background(
                                    if (index == safeStep) Primary else BorderCard,
                                    CircleShape
                                )
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SecondaryButton(
                        label = stringResource(R.string.cta_back),
                        onClick = onBack,
                        enabled = safeStep > 0,
                        accent = visuals.dangerAccent.copy(alpha = 0.35f),
                        modifier = Modifier.height(52.dp)
                    )
                    PrimaryButton(
                        label = if (safeStep >= TutorialViewModel.LAST_STEP) {
                            stringResource(R.string.cta_done)
                        } else {
                            stringResource(R.string.cta_next)
                        },
                        onClick = onNext,
                        height = 52.dp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
