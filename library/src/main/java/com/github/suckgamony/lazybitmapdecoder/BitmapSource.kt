package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

abstract class BitmapSource {
    abstract val densityScalingSupported: Boolean

    internal abstract fun decodeBitmap(options: BitmapFactory.Options): Bitmap?

    internal abstract fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap?

    internal open fun onDecodeStarted() {
    }

    internal open fun onDecodeFinished() {
    }
}
