package com.github.skgmn.mochka.decoder

import android.graphics.Bitmap
import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.DecodingParametersBuilder
import com.github.skgmn.mochka.util.AspectRatioCalculator

internal class MutableBitmapDecoder(
    other: BitmapDecoder,
    private val mutable: Boolean
) : BitmapDecoderWrapper(other) {
    override fun mutable(mutable: Boolean): BitmapDecoder {
        return if (this.mutable == mutable) {
            this
        } else {
            other.mutable(mutable)
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return other.makeParameters(flags).apply {
            mutable = this@MutableBitmapDecoder.mutable
        }
    }
}
