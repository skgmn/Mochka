package com.github.skgmn.mochka

import android.graphics.BitmapFactory
import android.graphics.Rect
import com.github.skgmn.mochka.DecodingParameters

internal class DecodingParametersBuilder(
    var scaleX: Float = 1f,
    var scaleY: Float = 1f,
    var region: Rect? = null
) {
    fun buildParameters(): DecodingParameters {
        val options = BitmapFactory.Options()

        var sampleSize = 1
        var sx = scaleX
        var sy = scaleY
        while (sx <= 0.5f && sy <= 0.5f) {
            sampleSize *= 2
            sx *= 2f
            sy *= 2f
        }
        options.inSampleSize = sampleSize

        return DecodingParameters(
            options = options,
            postScaleX = sx,
            postScaleY = sy,
            region = region
        )
    }

    companion object {
        internal const val FLAG_REGIONAL = 1
    }
}
