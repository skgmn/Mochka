package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal class SourceBitmapDecoder(
    internal val source: BitmapSource
) : BaseSourceBitmapDecoder() {
    override val width: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return widthDecoded
            }
        }
    override val height: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds(source)
                return heightDecoded
            }
        }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return if ((flags and DecodingParametersBuilder.FLAG_REGIONAL) != 0) {
            val parametersBuilder = super.makeParameters(flags)
            if (source.manualDensityScalingForRegional) {
                decodeBounds(source)
                parametersBuilder.scaleX *= densityScale
                parametersBuilder.scaleY *= densityScale
            }
            parametersBuilder
        } else {
            super.makeParameters(flags)
        }
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        source.onDecodeStarted()
        try {
            val params = parametersBuilder.buildParameters()
            val bitmap = if (params.region != null) {
                source.decodeBitmapRegion(params.region, params.options)
            } else {
                source.decodeBitmap(params.options).also {
                    synchronized(boundsDecodeLock) {
                        if (!boundsDecoded) {
                            copyMetadata(params.options)
                        }
                    }
                }
            }
            return postProcess(bitmap, params)
        } finally {
            source.onDecodeFinished()
        }
    }
}
