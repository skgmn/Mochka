package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.RectF

interface BitmapSource {
    val densityScalingSupported: Boolean

    fun decodeBitmap(options: BitmapFactory.Options): Bitmap?

    fun decodeBitmapRegion(rect: Rect, options: BitmapFactory.Options): Bitmap?
}
