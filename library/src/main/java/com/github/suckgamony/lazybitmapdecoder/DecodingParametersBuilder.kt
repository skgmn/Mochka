package com.github.suckgamony.lazybitmapdecoder

import android.graphics.BitmapFactory
import android.graphics.Rect

internal data class DecodingParametersBuilder(
    val decodingOptions: DecodingOptions,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val region: Rect? = null
) {
    fun buildParameters(): DecodingParameters {
        val options = BitmapFactory.Options()
        decodingOptions.fillInOptions(options)

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
            decodingOptions = decodingOptions,
            postScaleX = sx,
            postScaleY = sy,
            region = region
        )
    }
}
