package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import android.graphics.Color
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import java.util.*
import kotlin.math.absoluteValue

open class InstrumentedTestBase {
    fun <T> assertNotNull(obj: T?): T {
        Assert.assertNotNull(obj)
        return obj!!
    }

    fun assertEquals(bitmap1: Bitmap, bitmap2: Bitmap, tolerance: Int = 0) {
        assertTrue(equals(bitmap1, bitmap2, tolerance))
    }

    private fun equals(bitmap1: Bitmap, bitmap2: Bitmap, tolerance: Int): Boolean {
        if (bitmap1.width != bitmap2.width ||
            bitmap1.height != bitmap2.height ||
            bitmap1.config != bitmap2.config
        ) {
            return false
        }
        val width = bitmap1.width
        val pixels1 = IntArray(width)
        val pixels2 = IntArray(width)
        for (i in 0 until bitmap1.height) {
            bitmap1.getPixels(pixels1, 0, width, 0, i, width, 1)
            bitmap2.getPixels(pixels2, 0, width, 0, i, width, 1)
            if (tolerance == 0) {
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
                        return false
                    }
                }
            }
        }
        return true
    }

    companion object {
        const val MAX_TOLERENCE = 0x0c
    }
}
