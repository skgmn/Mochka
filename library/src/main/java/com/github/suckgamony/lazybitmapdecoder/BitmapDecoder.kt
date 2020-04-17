package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.Matrix
import com.github.suckgamony.lazybitmapdecoder.decoder.ScaleByBitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.decoder.ScaleToBitmapDecoder

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    open fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return ScaleToBitmapDecoder(this, width, height)
    }

    open fun scaleBy(scaleWidth: Float, scaleHeight: Float): BitmapDecoder {
        return ScaleByBitmapDecoder(this, scaleWidth, scaleHeight)
    }

    fun decode(): Bitmap? {
        return decode(makeParameters())
    }

    internal abstract fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap?

    internal open fun makeParameters(): DecodingParametersBuilder {
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
