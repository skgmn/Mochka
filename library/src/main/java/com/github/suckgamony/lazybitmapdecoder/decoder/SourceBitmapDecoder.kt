package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingOptions
import com.github.suckgamony.lazybitmapdecoder.DecodingParameters

internal class SourceBitmapDecoder(
    private val source: BitmapSource
) : BitmapDecoder() {
    private val state = source.createState()

    override val width: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return widthDecoded
            }
        }
    override val height: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return heightDecoded
            }
        }

    override val sourceWidth: Int
        get() = width
    override val sourceHeight: Int
        get() = height

    override fun decode(decodingParameters: DecodingParameters): Bitmap? {
        state.startDecode()
        val options = decodingParameters.decodingOptions.toBitmapFactoryOptions()
        val bitmap = source.decodeBitmap(options)
        synchronized(boundsDecodeLock) {
            if (!boundsDecoded) {
                copyMetadata(options)
            }
        }
        return bitmap
    }
}
