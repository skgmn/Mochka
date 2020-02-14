package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingOptions

internal class DefaultBitmapDecoder(
    source: BitmapSource
) : BitmapDecoder(source) {
    private val state = source.createState()

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

    override fun decode(decodingOptions: DecodingOptions): Bitmap? {
        state.startDecode()
        val options = decodingOptions.toBitmapFactoryOptions()
        val bitmap = source.decodeBitmap(options)
        synchronized(boundsDecodeLock) {
            if (!boundsDecoded) {
                copyMetadata(options)
            }
        }
        return bitmap
    }

    @GuardedBy("boundsDecodeLock")
    private fun decodeBounds() {
        if (boundsDecoded) {
            return
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        source.decodeBitmap(options)
        copyMetadata(options)
    }

    @GuardedBy("boundsDecodeLock")
    private fun copyMetadata(options: BitmapFactory.Options) {
        widthDecoded = options.outWidth
        heightDecoded = options.outHeight
    }
}
