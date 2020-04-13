package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal class SourceBitmapDecoder(
    private val source: BitmapSource
) : BaseSourceBitmapDecoder() {
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

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        state.startDecode()
        try {
            val params = parametersBuilder.buildParameters()
            val bitmap = source.decodeBitmap(state, params.options)
            synchronized(boundsDecodeLock) {
                if (!boundsDecoded) {
                    copyMetadata(params.options)
                }
            }
            return postProcess(bitmap, params)
        } finally {
            state.finishDecode()
        }
    }
}
