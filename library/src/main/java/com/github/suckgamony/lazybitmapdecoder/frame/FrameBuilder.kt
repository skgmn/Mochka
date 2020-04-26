package com.github.suckgamony.lazybitmapdecoder.frame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder

internal abstract class FrameBuilder(
    private val decoder: BitmapDecoder,
    protected var frameWidth: Int,
    protected var frameHeight: Int,
    private val background: Drawable?
) {
    protected abstract fun getBounds(
        decoder: BitmapDecoder,
        frameWidth: Int,
        frameHeight: Int,
        outSrc: Rect?,
        outDest: Rect?
    )

    private fun setRegion(
        decoder: BitmapDecoder,
        frameWidth: Int,
        frameHeight: Int,
        destRegion: Rect?
    ): BitmapDecoder {
        val sourceRegion = Rect()
        getBounds(decoder, frameWidth, frameHeight, sourceRegion, destRegion)
        return if (
            sourceRegion.left == 0 &&
            sourceRegion.top == 0 &&
            sourceRegion.right == decoder.width &&
            sourceRegion.bottom == decoder.height
        ) {
            decoder
        } else {
            decoder.region(sourceRegion)
        }
    }

    fun decode(): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        background?.apply {
            setBounds(0, 0, bitmap.width, bitmap.height)
            draw(canvas)
        }

        val rectDest = Rect()
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        val decodedBitmap = setRegion(decoder, frameWidth, frameHeight, rectDest).decode()
        decodedBitmap?.let {
            canvas.drawBitmap(it, null, rectDest, paint)
        }

        return bitmap
    }

    companion object {
        @JvmStatic
        fun newInstance(
            decoder: BitmapDecoder,
            scaleType: ImageView.ScaleType,
            frameWidth: Int,
            frameHeight: Int,
            background: Drawable?
        ): FrameBuilder {
            return when {
                ImageView.ScaleType.MATRIX == scaleType -> {
                    MatrixFrameBuilder(decoder, frameWidth, frameHeight, background)
                }
                ImageView.ScaleType.FIT_XY == scaleType -> {
                    FitXYFrameBuilder(decoder, frameWidth, frameHeight, background)
                }
                ImageView.ScaleType.FIT_START == scaleType -> {
                    FitGravityFrameBuilder(
                        decoder, frameWidth, frameHeight, background,
                        FitGravityFrameBuilder.GRAVITY_START
                    )
                }
                ImageView.ScaleType.FIT_CENTER == scaleType -> {
                    FitGravityFrameBuilder(
                        decoder, frameWidth, frameHeight, background,
                        FitGravityFrameBuilder.GRAVITY_CENTER
                    )
                }
                ImageView.ScaleType.FIT_END == scaleType -> {
                    FitGravityFrameBuilder(
                        decoder, frameWidth, frameHeight, background,
                        FitGravityFrameBuilder.GRAVITY_END
                    )
                }
                ImageView.ScaleType.CENTER == scaleType -> {
                    CenterFrameBuilder(decoder, frameWidth, frameHeight, background)
                }
                ImageView.ScaleType.CENTER_CROP == scaleType -> {
                    CenterCropFrameBuilder(decoder, frameWidth, frameHeight, background)
                }
                ImageView.ScaleType.CENTER_INSIDE == scaleType -> {
                    CenterInsideFrameBuilder(decoder, frameWidth, frameHeight, background)
                }
                else -> {
                    throw IllegalArgumentException("scaleType")
                }
            }
        }
    }
}
