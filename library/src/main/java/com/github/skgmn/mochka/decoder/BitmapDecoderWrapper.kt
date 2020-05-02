package com.github.skgmn.mochka.decoder

import android.graphics.Bitmap
import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder

internal abstract class BitmapDecoderWrapper(
    internal val other: BitmapDecoder
): BitmapDecoder() {
    override val width: Int
        get() = other.width
    override val height: Int
        get() = other.height
    override val mimeType: String
        get() = other.mimeType

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags)
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        return other.decode(parametersBuilder)
    }
}
