package com.Hofhofo8.Qogfad.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.di.ServiceLocator
import com.Hofhofo8.Qogfad.core.theme.BgDeep
import com.Hofhofo8.Qogfad.core.theme.BorderHair
import com.Hofhofo8.Qogfad.core.theme.Ember
import com.Hofhofo8.Qogfad.core.theme.LocalAppVisuals
import com.Hofhofo8.Qogfad.core.theme.Primary
import com.Hofhofo8.Qogfad.core.theme.SurfaceElevated
import com.Hofhofo8.Qogfad.core.theme.SurfaceSilhouette
import com.Hofhofo8.Qogfad.core.theme.TextPrimary
import com.Hofhofo8.Qogfad.core.theme.TextSecondary
import com.Hofhofo8.Qogfad.core.theme.TextTertiary
import com.Hofhofo8.Qogfad.core.ui.ScreenScaffold

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val visuals = LocalAppVisuals.current
    val transition = rememberInfiniteTransition(label = "splash")
    val bob by transition.animateFloatAsStateCompat(
        from = 0.96f,
        to = 1.04f,
        durationMs = 2600,
        label = "bob",
        enabled = !visuals.reducedMotion
    )
    val swing by transition.animateFloatAsStateCompat(
        from = -7f,
        to = 7f,
        durationMs = 3200,
        label = "swing",
        enabled = !visuals.reducedMotion
    )
    val drift by transition.animateFloatAsStateCompat(
        from = 0f,
        to = 1f,
        durationMs = 14000,
        label = "drift",
        easingLinear = true,
        enabled = !visuals.reducedMotion
    )

    ScreenScaffold(
        overlay = listOf(
            BgDeep.copy(alpha = 0.92f),
            BgDeep.copy(alpha = 0.96f),
            Color.Black.copy(alpha = 0.98f)
        )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val base = size.height * 0.80f
            val peaks = listOf(0.18f to 0.30f, 0.50f to 0.42f, 0.82f to 0.26f)
            peaks.forEachIndexed { index, pair ->
                val shift = (index * 6f) * drift
                val path = Path().apply {
                    val cx = size.width * pair.first + shift
                    val halfWidth = size.width * 0.16f
                    val peakY = base - size.height * pair.second
                    moveTo(cx - halfWidth, base)
                    lineTo(cx, peakY)
                    lineTo(cx + halfWidth, base)
                    close()
                }
                drawPath(path = path, color = SurfaceSilhouette.copy(alpha = 0.35f))
            }
            drawRect(
                color = SurfaceSilhouette.copy(alpha = 0.22f),
                topLeft = Offset(0f, base),
                size = Size(size.width, size.height - base)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(188.dp)
                    .graphicsLayer {
                        scaleX = bob
                        scaleY = bob
                    },
                shape = CircleShape,
                color = Primary.copy(alpha = 0.10f),
                border = BorderStroke(visuals.hairline, BorderHair),
                shadowElevation = 18.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_brand_crest),
                        modifier = Modifier.size(148.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(40.dp)
                    .height(72.dp)
                    .graphicsLayer { rotationZ = swing }
            ) {
                val cx = size.width / 2f
                drawLine(
                    color = Primary.copy(alpha = 0.85f),
                    start = Offset(cx, 0f),
                    end = Offset(cx, size.height - 10f),
                    strokeWidth = 4f
                )
                drawCircle(
                    color = Ember,
                    radius = 10f,
                    center = Offset(cx, size.height - 6f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.brand_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(34.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(184.dp)
                    .height(5.dp),
                color = Primary,
                trackColor = SurfaceElevated,
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_status),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 34.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceElevated.copy(alpha = 0.55f),
                border = BorderStroke(visuals.hairline, BorderHair)
            ) {
                Text(
                    text = stringResource(R.string.splash_footer),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }
    }
}

@Composable
private fun InfiniteTransition.animateFloatAsStateCompat(
    from: Float,
    to: Float,
    durationMs: Int,
    label: String,
    easingLinear: Boolean = false,
    enabled: Boolean = true
) = animateFloat(
    initialValue = from,
    targetValue = if (enabled) to else from,
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = durationMs,
            easing = if (easingLinear) LinearEasing else FastOutSlowInEasing
        ),
        repeatMode = RepeatMode.Reverse
    ),
    label = label
)
