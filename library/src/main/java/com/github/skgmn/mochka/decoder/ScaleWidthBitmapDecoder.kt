package com.github.skgmn.mochka.decoder

import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder
import com.github.skgmn.mochka.util.AspectRatioCalculator

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

    override fun scaleHeight(height: Int): BitmapDecoder {
        return other.scaleHeight(height)
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        val scale = width.toFloat() / other.width
        return other.makeParameters(flags).apply {
            scaleX *= scale
            scaleY *= scale
        }
    }
}
