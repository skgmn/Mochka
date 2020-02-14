package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

abstract class BitmapSource {
    abstract val densityScalingSupported: Boolean

    abstract fun decodeBitmap(options: BitmapFactory.Options): Bitmap?

    abstract fun decodeBitmapRegion(rect: Rect, options: BitmapFactory.Options): Bitmap?

    internal open fun createState(): DecodingState = DecodingState()

    internal open fun createRegionalState(): DecodingState = DecodingState()
}
