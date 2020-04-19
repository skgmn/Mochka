package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder
import kotlin.math.roundToInt

internal class RegionalSourceBitmapDecoder(
    private val source: BitmapSource,
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : BaseSourceBitmapDecoder() {
    override val state = source.createRegionalState()

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
            RegionalSourceBitmapDecoder(
                source,
                newLeft,
                newTop,
                newLeft + (right - left),
                newTop + (bottom - top)
            )
        }
    }

    override fun makeParameters(): DecodingParametersBuilder {
        val densityScale = if (source.densityScalingSupported) {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                densityScale
            }
        } else {
            1f
        }

        val scaledRegion = Rect(
            (left / densityScale).roundToInt(),
            (top / densityScale).roundToInt(),
            (right / densityScale).roundToInt(),
            (bottom / densityScale).roundToInt()
        )

        return DecodingParametersBuilder(
            scaleX = width.toFloat() / scaledRegion.width(),
            scaleY = height.toFloat() / scaledRegion.height(),
            region = scaledRegion
        )
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        state.startDecode()
        try {
            val params = parametersBuilder.buildParameters()
            checkNotNull(params.region)
            val bitmap = source.decodeBitmapRegion(state, params.region, params.options)
            return postProcess(bitmap, params)
        } finally {
            state.finishDecode()
        }
    }
}
