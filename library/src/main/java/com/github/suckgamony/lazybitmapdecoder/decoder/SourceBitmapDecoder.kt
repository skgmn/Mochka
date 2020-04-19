package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.GuardedBy
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecodingParametersBuilder

internal class SourceBitmapDecoder(
    internal val source: BitmapSource
) : BitmapDecoder() {
    private val boundsDecodeLock = Any()

    @GuardedBy("boundsDecodeLock")
    private var widthDecoded = -1
    @GuardedBy("boundsDecodeLock")
    private var heightDecoded = -1
    @GuardedBy("boundsDecodeLock")
    private var densityScale = 1f

    private val boundsDecoded
        @GuardedBy("boundsDecodeLock")
        get() = widthDecoded != -1

    override val width: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds()
                return widthDecoded
            }
        }
    override val height: Int
        get() {
            synchronized(boundsDecodeLock) {
                decodeBounds()
                return heightDecoded
            }
        }

    @GuardedBy("boundsDecodeLock")
    private fun decodeBounds() {
        if (boundsDecoded) {
            return
        }

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        source.decodeBitmap(options)
        copyMetadata(options)
    }

    @GuardedBy("boundsDecodeLock")
    private fun copyMetadata(options: BitmapFactory.Options) {
        widthDecoded = options.outWidth
        heightDecoded = options.outHeight
        if (options.inScaled && options.inDensity != 0 && options.inTargetDensity != 0) {
            densityScale = options.inTargetDensity.toFloat() / options.inDensity
        }
    }

    override fun makeParameters(flags: Int): DecodingParametersBuilder {
        return if ((flags and DecodingParametersBuilder.FLAG_REGIONAL) != 0) {
            val parametersBuilder = super.makeParameters(flags)
            if (source.manualDensityScalingForRegional) {
                synchronized(boundsDecodeLock) {
                    decodeBounds()
                    parametersBuilder.scaleX *= densityScale
                    parametersBuilder.scaleY *= densityScale
                }
            }
            parametersBuilder
        } else {
            super.makeParameters(flags)
        }
    }

    override fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap? {
        val params = parametersBuilder.buildParameters()
        if (params.region != null) {
            synchronized(boundsDecodeLock) {
                decodeBounds()
            }
        }

        source.onDecodeStarted()
        try {
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
