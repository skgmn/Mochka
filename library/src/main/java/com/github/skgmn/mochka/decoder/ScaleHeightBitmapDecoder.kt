package com.github.skgmn.mochka.decoder

import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder
import com.github.skgmn.mochka.util.AspectRatioCalculator

internal class ScaleHeightBitmapDecoder(
    other: BitmapDecoder,
    override val height: Int
) : BitmapDecoderWrapper(other) {
    override val width: Int by lazy {
        AspectRatioCalculator.getWidth(other.width, other.height, height)
    }

    override fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return other.scaleTo(width, height)
    }

    override fun scaleWidth(width: Int): BitmapDecoder {
        return other.scaleWidth(width)
    }

    override fun scaleHeight(height: Int): BitmapDecoder {
        return if (this.height == height) {
            this
        } else {
            other.scaleHeight(height)
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        val scale = height.toFloat() / other.height
        return other.makeParameters(flags).apply {
            scaleX *= scale
            scaleY *= scale
        }
    }
}
