package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParameters

internal class SourceBitmapDecoder(
    private val source: BitmapSource
) : BitmapDecoder() {
    override val state = source.createState()

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

    override fun decode(parameters: DecodingParameters): Bitmap? {
        state.startDecode()
        try {
            val options = parameters.createOptions()
            val bitmap = source.decodeBitmap(state, options)
            synchronized(boundsDecodeLock) {
                if (!boundsDecoded) {
                    copyMetadata(options)
                }
            }
            return bitmap
        } finally {
            state.finishDecode()
        }
    }
}
