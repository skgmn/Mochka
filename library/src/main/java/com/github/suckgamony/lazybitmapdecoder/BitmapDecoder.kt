package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.Matrix

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    fun decode(): Bitmap? {
        return decode(fillInParameters())
    }

    internal abstract fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap?

    internal open fun fillInParameters(): DecodingParametersBuilder {
        return DecodingParametersBuilder()
    }

    internal fun postProcess(bitmap: Bitmap?, params: DecodingParameters): Bitmap? {
        return if (bitmap == null || params.postScaleX == 1f && params.postScaleY == 1f) {
            bitmap
        } else {
            val m = Matrix()
            m.setScale(params.postScaleX, params.postScaleY)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }
}
