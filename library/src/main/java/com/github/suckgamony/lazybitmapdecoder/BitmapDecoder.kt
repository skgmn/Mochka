package com.github.suckgamony.lazybitmapdecoder

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import androidx.annotation.DrawableRes
import com.github.suckgamony.lazybitmapdecoder.decoder.RegionBitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.decoder.ScaleByBitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.decoder.ScaleToBitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.decoder.SourceBitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.source.*
import com.github.suckgamony.lazybitmapdecoder.source.ByteArrayBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.FileBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.InMemoryBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.InputStreamBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import java.io.File
import java.io.InputStream

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    open fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return ScaleToBitmapDecoder(this, width, height)
    }

    open fun scaleBy(scaleWidth: Float, scaleHeight: Float): BitmapDecoder {
        return if (scaleWidth == 1f && scaleHeight == 1f) {
            this
        } else {
            ScaleByBitmapDecoder(this, scaleWidth, scaleHeight)
        }
    }

    open fun region(left: Int, top: Int, right: Int, bottom: Int): BitmapDecoder {
        return RegionBitmapDecoder(this, left, top, right, bottom)
    }

    fun region(bounds: Rect): BitmapDecoder {
        return region(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    fun decode(): Bitmap? {
        return decode(makeParameters(0))
    }

    internal abstract fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap?

    internal open fun makeParameters(flags: Int): DecodingParametersBuilder {
        return DecodingParametersBuilder()
    }

    internal fun postProcess(bitmap: Bitmap?, params: DecodingParameters): Bitmap? {
        return if (bitmap == null || params.postScaleX == 1f && params.postScaleY == 1f) {
            bitmap
        } else {
            val m = Matrix()
            m.setScale(params.postScaleX, params.postScaleY)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }

    companion object {
        fun fromAsset(context: Context, path: String): BitmapDecoder {
            return SourceBitmapDecoder(AssetBitmapSource(context.assets, path))
        }

        fun fromAsset(assetManager: AssetManager, path: String): BitmapDecoder {
            return SourceBitmapDecoder(AssetBitmapSource(assetManager, path))
        }

        @JvmStatic
        fun fromBitmap(bitmap: Bitmap): BitmapDecoder {
            return SourceBitmapDecoder(InMemoryBitmapSource(bitmap))
        }

        @JvmStatic
        fun fromByteArray(array: ByteArray): BitmapDecoder {
            return fromByteArray(array, 0, array.size)
        }

        @JvmStatic
        fun fromByteArray(array: ByteArray, offset: Int, length: Int): BitmapDecoder {
            return SourceBitmapDecoder(ByteArrayBitmapSource(array, offset, length))
        }

        @JvmStatic
        fun fromFile(file: File): BitmapDecoder {
            return SourceBitmapDecoder(FileBitmapSource(file))
        }

        @JvmStatic
        fun fromResource(context: Context, @DrawableRes id: Int): BitmapDecoder {
            return SourceBitmapDecoder(ResourceBitmapSource(context.resources, id))
        }

        @JvmStatic
        fun fromResource(res: Resources, @DrawableRes id: Int): BitmapDecoder {
            return SourceBitmapDecoder(ResourceBitmapSource(res, id))
        }

        @JvmStatic
        fun fromStream(stream: InputStream): BitmapDecoder {
            return SourceBitmapDecoder(InputStreamBitmapSource(stream))
        }
    }
}
