package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal abstract class BitmapDecoderWrapper(
    internal val other: BitmapDecoder
): BitmapDecoder() {
    override val width: Int
        get() = other.width
    override val height: Int
        get() = other.height

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags)
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        return other.decode(parametersBuilder)
    }
}
