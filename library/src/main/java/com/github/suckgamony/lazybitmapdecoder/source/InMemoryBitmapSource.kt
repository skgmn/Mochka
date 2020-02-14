package com.github.suckgamony.lazybitmapdecoder.source

import android.graphics.*
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecoderState

class InMemoryBitmapSource(private val bitmap: Bitmap) : BitmapSource() {
    override val densityScalingSupported: Boolean
        get() = false

    override fun decodeBitmap(state: DecoderState, options: BitmapFactory.Options): Bitmap? {
        val sampleSize = options.inSampleSize
        val bitmapWidth = Math.ceil(bitmap.width / sampleSize.toDouble()).toInt()
        val bitmapHeight = Math.ceil(bitmap.height / sampleSize.toDouble()).toInt()

        options.outWidth = bitmapWidth
        options.outHeight = bitmapHeight
        options.outMimeType = MIME_TYPE
        return if (options.inJustDecodeBounds) {
            null
        } else {
            if (options.inMutable) {
                Bitmap.createBitmap(bitmapWidth, bitmapHeight, bitmap.config).also { newBitmap ->
                    val canvas = Canvas(newBitmap)
                    canvas.drawBitmap(bitmap, 0f, 0f, null)
                }
            } else {
                if (bitmap.isMutable) {
                    Bitmap.createBitmap(bitmap)
                } else {
                    bitmap
                }
            }
        }
    }

    override fun decodeBitmapRegion(state: DecoderState, rect: Rect, options: BitmapFactory.Options): Bitmap? {
        val sampleSize = options.inSampleSize
        val regionWidth = rect.width() / sampleSize
        val regionHeight = rect.height() / sampleSize

        options.outWidth = regionWidth
        options.outHeight = regionHeight
        options.outMimeType = MIME_TYPE
        return if (options.inJustDecodeBounds) {
            null
        } else {
            val partialBitmap = Bitmap.createBitmap(regionWidth, regionHeight, bitmap.config).also { newBitmap ->
                val dst = RectF(0f, 0f, regionWidth.toFloat(), regionHeight.toFloat())
                val paint = Paint(Paint.FILTER_BITMAP_FLAG)
                val canvas = Canvas(newBitmap)
                canvas.drawBitmap(bitmap, rect, dst, paint)
            }
            if (options.inMutable) {
                partialBitmap
            } else {
                Bitmap.createBitmap(partialBitmap)
            }
        }
    }

    companion object {
        private const val MIME_TYPE = "image/bmp"
    }
}
