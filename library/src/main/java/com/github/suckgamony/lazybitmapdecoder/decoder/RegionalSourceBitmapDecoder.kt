package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingOptions
import com.github.suckgamony.lazybitmapdecoder.DecodingParameters

internal class RegionalSourceBitmapDecoder(
    private val source: BitmapSource,
    rect: Rect
) : BitmapDecoder() {
    private val state = source.createRegionalState()
    private val coercedRect by lazy {
        Rect(
            rect.left.coerceIn(0, sourceWidth),
            rect.top.coerceIn(0, sourceHeight),
            rect.right.coerceIn(0, sourceWidth),
            rect.bottom.coerceIn(0, sourceHeight)
        )
    }

    override val width: Int
        get() = coercedRect.width()
    override val height: Int
        get() = coercedRect.height()

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

    override fun decode(decodingParameters: DecodingParameters): Bitmap? {
        state.startDecode()
        val options = decodingParameters.decodingOptions.toBitmapFactoryOptions()
        return source.decodeBitmapRegion(coercedRect, options)
    }
}
