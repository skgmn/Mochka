package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.annotation.GuardedBy

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    abstract val sourceWidth: Int
    abstract val sourceHeight: Int

    fun decode(decodingOptions: DecodingOptions = DecodingOptions()): Bitmap? {
        return decode(fillInParameters(decodingOptions))
    }

    internal abstract fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap?

    internal open fun fillInParameters(decodingOptions: DecodingOptions): DecodingParametersBuilder {
        return DecodingParametersBuilder(decodingOptions)
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
