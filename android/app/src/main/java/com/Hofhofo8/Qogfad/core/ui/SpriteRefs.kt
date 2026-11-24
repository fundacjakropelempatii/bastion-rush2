package com.Hofhofo8.Qogfad.core.ui

import androidx.annotation.DrawableRes
import com.Hofhofo8.Qogfad.R
import com.Hofhofo8.Qogfad.domain.model.BlockFamily

@DrawableRes
fun familySprite(family: BlockFamily): Int = when (family) {
    BlockFamily.GRANITE -> R.drawable.sprite_1
    BlockFamily.TIMBER -> R.drawable.sprite_2
    BlockFamily.KEYSTONE -> R.drawable.sprite_3
}
