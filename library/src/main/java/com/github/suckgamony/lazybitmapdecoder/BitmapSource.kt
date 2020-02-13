package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF

abstract class BitmapSource {
    abstract val densityScalingSupported: Boolean

    abstract fun decodeBitmap(options: BitmapFactory.Options): Bitmap?

    abstract fun decodeBitmapRegion(rect: Rect, options: BitmapFactory.Options): Bitmap?

    internal open fun createState(): DecodeState = DecodeState()

    internal open fun createRegionalState(): DecodeState = DecodeState()
}
