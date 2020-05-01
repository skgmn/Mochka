package com.github.skgmn.mochka.decoder

import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder
import kotlin.math.roundToInt

internal class ScaleByBitmapDecoder(
    other: BitmapDecoder,
    private val scaleX: Float,
    private val scaleY: Float
) : BitmapDecoderWrapper(other) {
    override val width: Int by lazy {
        (other.width * scaleX).roundToInt()
    }
    override val height: Int by lazy {
        (other.height * scaleY).roundToInt()
    }

    override fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return other.scaleTo(width, height)
    }

    override fun scaleBy(scaleWidth: Float, scaleHeight: Float): BitmapDecoder {
        return if (scaleWidth == 1f && scaleHeight == 1f) {
            this
        } else {
            val sx = this.scaleX * scaleWidth
            val sy = this.scaleY * scaleHeight
            if (sx == 1f && sy == 1f) {
                other
            } else {
                other.scaleBy(sx, sy)
            }
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags).apply {
            scaleX *= this@ScaleByBitmapDecoder.scaleX
            scaleY *= this@ScaleByBitmapDecoder.scaleY
        }
    }
}
