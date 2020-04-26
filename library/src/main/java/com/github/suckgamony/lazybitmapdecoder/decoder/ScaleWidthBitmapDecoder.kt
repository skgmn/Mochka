package com.github.suckgamony.lazybitmapdecoder.decoder

import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder
import com.github.suckgamony.lazybitmapdecoder.util.AspectRatioCalculator

internal class ScaleWidthBitmapDecoder(
    other: BitmapDecoder,
    override val width: Int
) : BitmapDecoderWrapper(other) {
    override val height: Int by lazy {
        AspectRatioCalculator.getHeight(other.width, other.height, width)
    }

    override fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return other.scaleTo(width, height)
    }

    override fun scaleWidth(width: Int): BitmapDecoder {
        return if (this.width == width) {
            this
        } else {
            other.scaleWidth(width)
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        val scale = width.toFloat() / other.width
        return other.makeParameters(flags).apply {
            scaleX *= scale
            scaleY *= scale
        }
    }
}
