package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingOptions
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal class RegionalSourceBitmapDecoder(
    private val source: BitmapSource,
    region: Rect
) : BitmapDecoder() {
    override val state = source.createRegionalState()
    private val coercedRegion by lazy {
        Rect(
            region.left.coerceIn(0, sourceWidth),
            region.top.coerceIn(0, sourceHeight),
            region.right.coerceIn(0, sourceWidth),
            region.bottom.coerceIn(0, sourceHeight)
        )
    }

    override val width: Int
        get() = coercedRegion.width()
    override val height: Int
        get() = coercedRegion.height()

    override val sourceWidth: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return widthDecoded
            }
        }
    override val sourceHeight: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return heightDecoded
            }
        }

    override fun fillInParameters(decodingOptions: DecodingOptions): DecodingParametersBuilder {
        return DecodingParametersBuilder(
            decodingOptions = decodingOptions,
            region = coercedRegion
        )
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        state.startDecode()
        try {
            val params = parametersBuilder.buildParameters()
            return source.decodeBitmapRegion(state, parametersBuilder.region ?: coercedRegion, params.options)
        } finally {
            state.finishDecode()
        }
    }
}
