package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodeOptions
import com.github.suckgamony.lazybitmapdecoder.DecodeState

internal class DefaultBitmapDecoder(
    source: BitmapSource
) : BitmapDecoder(source) {
    private val boundsDecodeLock = Any()

    @GuardedBy("boundsDecodeLock")
    private var widthDecoded = -1
    @GuardedBy("boundsDecodeLock")
    private var heightDecoded = -1

    private val boundsDecoded
        @GuardedBy("boundsDecodeLock")
        get() = widthDecoded != -1

    override val width: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds()
                return widthDecoded
            }
        }
    override val height: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds()
                return heightDecoded
            }
        }

    override fun decode(options: DecodeOptions): Bitmap? {
        val state = source.createState()
        return decodeInternal(state, options)
    }

    override fun decodeInternal(state: DecodeState, options: DecodeOptions): Bitmap? {
        state.rewind()
        val bitmapFactoryOptions = options.toBitmapFactoryOptions()
        return source.decodeBitmap(bitmapFactoryOptions)
    }

    @GuardedBy("boundsDecodeLock")
    private fun decodeBounds() {
        if (boundsDecoded) {
            return
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        source.decodeBitmap(options)

        widthDecoded = options.outWidth
        heightDecoded = options.outHeight
    }
}
