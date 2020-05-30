package com.github.skgmn.mochka

import android.graphics.BitmapFactory
import android.graphics.Rect

internal class DecodingParameters(
    val options: BitmapFactory.Options,
    val postScaleX: Float = 1f,
    val postScaleY: Float = 1f,
    val region: Rect? = null
)
