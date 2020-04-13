package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecoderState

abstract class BaseSourceBitmapDecoder : BitmapDecoder() {
    internal abstract val state: DecoderState

    protected val boundsDecodeLock = Any()

    @GuardedBy("boundsDecodeLock")
    protected var widthDecoded = -1
    @GuardedBy("boundsDecodeLock")
    protected var heightDecoded = -1
    @GuardedBy("boundsDecodeLock")
    protected var densityScale = 1f

    protected val boundsDecoded
        @GuardedBy("boundsDecodeLock")
        get() = widthDecoded != -1

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
        if (options.inScaled && options.inDensity != 0 && options.inTargetDensity != 0) {
            densityScale = options.inTargetDensity.toFloat() / options.inDensity
        }
    }
}
