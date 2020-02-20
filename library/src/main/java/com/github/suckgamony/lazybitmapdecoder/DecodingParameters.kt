package com.github.suckgamony.lazybitmapdecoder

import android.graphics.BitmapFactory
import android.graphics.Rect

internal class DecodingParameters(
    val options: BitmapFactory.Options,
    val decodingOptions: DecodingOptions,
    val postScaleX: Float = 1f,
    val postScaleY: Float = 1f,
    val region: Rect? = null
)
