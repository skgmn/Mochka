package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    abstract val sourceWidth: Int
    abstract val sourceHeight: Int

    internal abstract val state: DecoderState

    protected val boundsDecodeLock = Any()

    @GuardedBy("boundsDecodeLock")
    protected var widthDecoded = -1
    @GuardedBy("boundsDecodeLock")
    protected var heightDecoded = -1

    protected val boundsDecoded
        @GuardedBy("boundsDecodeLock")
        get() = widthDecoded != -1

    fun decode(decodingOptions: DecodingOptions = DecodingOptions()): Bitmap? {
        return decode(fillInParameters(decodingOptions))
    }

    internal abstract fun decode(parameters: DecodingParameters): Bitmap?

    internal open fun fillInParameters(decodingOptions: DecodingOptions): DecodingParameters {
        return DecodingParameters(decodingOptions)
    }

    @GuardedBy("boundsDecodeLock")
    protected fun decodeBounds(source: BitmapSource) {
        if (boundsDecoded) {
            return
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        source.decodeBitmap(state, options)
        copyMetadata(options)
    }

    @GuardedBy("boundsDecodeLock")
    protected fun copyMetadata(options: BitmapFactory.Options) {
        widthDecoded = options.outWidth
        heightDecoded = options.outHeight
    }
}
