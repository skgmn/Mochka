package com.github.suckgamony.lazybitmapdecoder.decoder

import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal class ScaleToBitmapDecoder(
    other: BitmapDecoder,
    override val width: Int,
    override val height: Int
) : BitmapDecoderWrapper(other) {
    override fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return if (this.width == width && this.height == height) {
            this
        } else {
            other.scaleTo(width, height)
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags).apply {
            scaleX = width.toFloat() / other.width
            scaleY = height.toFloat() / other.height
        }
    }
}
