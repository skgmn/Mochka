package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

abstract class BitmapSource {
    abstract val densityScalingSupported: Boolean

    internal abstract fun decodeBitmap(state: DecoderState, options: BitmapFactory.Options): Bitmap?

    internal abstract fun decodeBitmapRegion(state: DecoderState, rect: Rect, options: BitmapFactory.Options): Bitmap?

    internal open fun createState(): DecoderState = DecoderState()

    internal open fun createRegionalState(): DecoderState = DecoderState()
}
