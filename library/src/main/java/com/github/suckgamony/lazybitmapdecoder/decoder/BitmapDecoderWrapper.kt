package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal abstract class BitmapDecoderWrapper(
    protected val other: BitmapDecoder
): BitmapDecoder() {
    override val width: Int
        get() = other.width
    override val height: Int
        get() = other.height

    override fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return other.scaleTo(width, height)
    }

    override fun makeParameters(): DecodingParametersBuilder {
        return other.makeParameters()
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        return other.decode(parametersBuilder)
    }
}
