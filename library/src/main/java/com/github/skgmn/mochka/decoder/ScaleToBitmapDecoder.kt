package com.github.skgmn.mochka.decoder

import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder
import com.github.skgmn.mochka.util.AspectRatioCalculator

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

    override fun scaleWidth(width: Int): BitmapDecoder {
        return if (this.width == width) {
            this
        } else {
            other.scaleTo(width, AspectRatioCalculator.getHeight(this.width, this.height, width))
        }
    }

    override fun scaleHeight(height: Int): BitmapDecoder {
        return if (this.height == height) {
            this
        } else {
            other.scaleTo(AspectRatioCalculator.getWidth(this.width, this.height, height), height)
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags).apply {
            scaleX *= width.toFloat() / other.width
            scaleY *= height.toFloat() / other.height
        }
    }
}
