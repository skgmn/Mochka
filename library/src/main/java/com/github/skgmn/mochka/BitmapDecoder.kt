package com.github.skgmn.mochka

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.github.skgmn.mochka.decoder.*
import com.github.skgmn.mochka.frame.FrameBuilder

abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int
    abstract val mimeType: String

    open fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return ScaleToBitmapDecoder(this, width, height)
    }

    open fun scaleWidth(width: Int): BitmapDecoder {
        return ScaleWidthBitmapDecoder(this, width)
    }

    open fun scaleHeight(height: Int): BitmapDecoder {
        return ScaleHeightBitmapDecoder(this, height)
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

    open fun mutable(mutable: Boolean): BitmapDecoder {
        return MutableBitmapDecoder(this, mutable)
    }

    @JvmOverloads
    fun frame(
        scaleType: ImageView.ScaleType,
        width: Int,
        height: Int,
        background: Drawable? = null
    ): Bitmap? {
        return FrameBuilder.newInstance(this, scaleType, width, height, background).decode()
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
}
