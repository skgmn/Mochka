package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder
import kotlin.math.roundToInt

internal class RegionalSourceBitmapDecoder(
    private val source: BitmapSource,
    private val region: Rect
) : BaseSourceBitmapDecoder() {
    override val state = source.createRegionalState()

    override val width: Int
        get() = region.width()
    override val height: Int
        get() = region.height()

    override fun makeParameters(): DecodingParametersBuilder {
        val densityScale = if (source.densityScalingSupported) {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                densityScale
            }
        } else {
            1f
        }

        val scaledRegion = if (densityScale == 1f) {
            region
        } else {
            Rect(region).apply {
                left = (left / densityScale).roundToInt()
                top = (top / densityScale).roundToInt()
                right = (right / densityScale).roundToInt()
                bottom = (bottom / densityScale).roundToInt()
            }
        }

        return DecodingParametersBuilder(
            scaleX = region.width().toFloat() / scaledRegion.width(),
            scaleY = region.height().toFloat() / scaledRegion.height(),
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
