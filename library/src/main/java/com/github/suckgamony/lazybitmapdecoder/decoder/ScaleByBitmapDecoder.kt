package com.github.suckgamony.lazybitmapdecoder.decoder

import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder
import kotlin.math.roundToInt

internal class ScaleByBitmapDecoder(
    other: BitmapDecoder,
    internal val scaleWidth: Float,
    internal val scaleHeight: Float
) : BitmapDecoderWrapper(other) {
    override val width: Int by lazy {
        (other.width * scaleWidth).roundToInt()
    }
    override val height: Int by lazy {
        (other.height * scaleHeight).roundToInt()
    }

    override fun scaleBy(scaleWidth: Float, scaleHeight: Float): BitmapDecoder {
        return if (scaleWidth == 1f && scaleHeight == 1f) {
            this
        } else {
            val sx = this.scaleWidth * scaleWidth
            val sy = this.scaleHeight * scaleHeight
            if (sx == 1f && sy == 1f) {
                other
            } else {
                ScaleByBitmapDecoder(other, sx, sy)
            }
        }
    }

    override fun makeParameters(): DecodingParametersBuilder {
        return other.makeParameters().apply {
            scaleX *= scaleWidth
            scaleY *= scaleHeight
        }
    }
}
