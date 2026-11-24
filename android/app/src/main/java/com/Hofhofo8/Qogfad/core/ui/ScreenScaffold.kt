package com.Hofhofo8.Qogfad.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.core.theme.BgBase

@Composable
fun ScreenScaffold(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    bloom: Color = Color.Transparent,
    bloomRadius: Float = 900f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        if (bloom != Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(bloom, Color.Transparent),
                            radius = bloomRadius
                        )
                    )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            content = content
        )
    }
}
