package com.github.suckgamony.lazybitmapdecoder

import android.graphics.BitmapFactory
import android.graphics.Rect

internal data class DecodingParameters(
    val decodingOptions: DecodingOptions,
    val scaleX: Float = 1f,
    val scaleY: Float = 1f,
    val region: Rect? = null
) {
    fun createOptions(): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        decodingOptions.fillInOptions(options)

        var sampleSize = 1
        var targetScaleX = scaleX
        var targetScaleY = scaleY
        while (targetScaleX <= 0.5f && targetScaleY <= 0.5f) {
            sampleSize *= 2
            targetScaleX *= 2f
            targetScaleY *= 2f
        }
        options.inSampleSize = sampleSize

        return options
    }
}
