package com.github.skgmn.mochka

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

internal abstract class BitmapSource {
    abstract val manualDensityScalingForRegional: Boolean

    internal abstract fun decodeBitmap(options: BitmapFactory.Options): Bitmap?

    internal abstract fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap?

    internal open fun onDecodeStarted() {
    }

    internal open fun onDecodeFinished() {
    }
}
