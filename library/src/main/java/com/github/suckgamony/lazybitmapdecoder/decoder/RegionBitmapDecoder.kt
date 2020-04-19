package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder
import kotlin.math.roundToInt

internal class RegionBitmapDecoder(
    other: BitmapDecoder,
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : BitmapDecoderWrapper(other) {
    override val width: Int
        get() = right - left
    override val height: Int
        get() = bottom - top

    override fun region(left: Int, top: Int, right: Int, bottom: Int): BitmapDecoder {
        return if (left == 0 && top == 0 && right == width && bottom == height) {
            this
        } else {
            val newLeft = this.left + left
            val newTop = this.top + top
            RegionBitmapDecoder(
                other,
                newLeft,
                newTop,
                newLeft + (right - left),
                newTop + (bottom - top)
            )
        }
    }

    override fun makeParameters(): DecodingParametersBuilder {
        return other.makeParameters().apply {
            val scaledRegion = region?.also {
                it.left += (left / scaleX).roundToInt()
                it.top += (top / scaleY).roundToInt()
                it.right = it.left + (width / scaleX).roundToInt()
                it.bottom = it.top + (height / scaleY).roundToInt()
            } ?: Rect(
                (left / scaleX).roundToInt(),
                (top / scaleY).roundToInt(),
                (right / scaleX).roundToInt(),
                (bottom / scaleY).roundToInt()
            )

            scaleX *= width.toFloat() / scaledRegion.width()
            scaleY *= height.toFloat() / scaledRegion.height()
            region = scaledRegion
        }
    }
}
