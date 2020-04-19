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

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        val newFlags = flags or DecodingParametersBuilder.FLAG_REGIONAL
        return other.makeParameters(newFlags).apply {
            val left = (region?.left ?: 0) + (left / scaleX).roundToInt()
            val top = (region?.top ?: 0) + (top / scaleY).roundToInt()
            val right = left + (width / scaleX).roundToInt()
            val bottom = top + (height / scaleY).roundToInt()

            val scaledRegion = region ?: Rect().also { region = it }
            scaledRegion.left = left
            scaledRegion.top = top
            scaledRegion.right = right
            scaledRegion.bottom = bottom

            scaleX = width.toFloat() / scaledRegion.width()
            scaleY = height.toFloat() / scaledRegion.height()
        }
    }
}
