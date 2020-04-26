package com.github.suckgamony.lazybitmapdecoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

open class InstrumentedTestBase {
    protected lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    fun <T> assertNotNull(obj: T?): T {
        Assert.assertNotNull(obj)
        return obj!!
    }

    fun assertEquals(bitmap1: Bitmap, bitmap2: Bitmap, tolerance: Int = 0, percentage: Float = 1f) {
        assertTrue(equals(bitmap1, bitmap2, tolerance, percentage))
    }

    fun decodeBitmapScaleTo(width: Int, height: Int, decoder: (BitmapFactory.Options?) -> Bitmap?): Bitmap {
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        decoder(opts)

        return decodeBitmapScaleBy(
            width.toFloat() / opts.outWidth,
            height.toFloat() / opts.outHeight,
            decoder
        )
    }

    fun decodeBitmapScaleBy(scaleX: Float, scaleY: Float, decoder: (BitmapFactory.Options?) -> Bitmap?): Bitmap {
        var sx = scaleX
        var sy = scaleY
        var sampleSize = 1
        while (sx <= 0.5f && sy <= 0.5f) {
            sx *= 2f
            sy *= 2f
            sampleSize *= 2
        }

        val opts = BitmapFactory.Options()
        opts.inSampleSize = sampleSize
        val bitmap = checkNotNull(decoder(opts))

        return Matrix().let { m ->
            m.setScale(sx, sy)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }

    fun scaleBy(bitmap: Bitmap, scaleX: Float, scaleY: Float): Bitmap {
        return Matrix().let { m ->
            m.setScale(scaleX, scaleY)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }

    private fun equals(bitmap1: Bitmap, bitmap2: Bitmap, tolerance: Int, percentage: Float): Boolean {
        val width = bitmap1.width
        val height = bitmap1.height

        if (width != bitmap2.width ||
            height != bitmap2.height ||
            bitmap1.config != bitmap2.config
        ) {
            return false
        }
        val pixels1 = IntArray(width)
        val pixels2 = IntArray(width)
        var failCount = 0
        for (i in 0 until height) {
            bitmap1.getPixels(pixels1, 0, width, 0, i, width, 1)
            bitmap2.getPixels(pixels2, 0, width, 0, i, width, 1)
            if (tolerance == 0 && percentage == 1f) {
                if (!pixels1.contentEquals(pixels2)) {
                    return false
                }
            } else {
                pixels1.forEachIndexed { index, pixel1 ->
                    val pixel2 = pixels2[index]
                    if ((Color.red(pixel1) - Color.red(pixel2)).absoluteValue > tolerance ||
                        (Color.green(pixel1) - Color.green(pixel2)).absoluteValue > tolerance ||
                        (Color.blue(pixel1) - Color.blue(pixel2)).absoluteValue > tolerance
                    ) {
                        if (percentage == 1f) {
                            return false
                        } else {
                            ++failCount
                        }
                    }
                }
            }
        }
        return failCount <= (width * height * (1f - percentage)).roundToInt()
    }

    companion object {
        const val MAX_TOLERENCE = 0x10
    }
}
