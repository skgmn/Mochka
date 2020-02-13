package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import java.util.*

open class InstrumentedTestBase {
    fun <T> assertNotNull(obj: T?): T {
        Assert.assertNotNull(obj)
        return obj!!
    }

    fun assertEquals(bitmap1: Bitmap, bitmap2: Bitmap) {
        assertTrue(equals(bitmap1, bitmap2))
    }

    private fun equals(bitmap1: Bitmap, bitmap2: Bitmap): Boolean {
        if (bitmap1.width != bitmap2.width ||
                bitmap1.height != bitmap2.height ||
                bitmap1.config != bitmap2.config) {
            return false
        }
        val width = bitmap1.width
        val pixels1 = IntArray(width)
        val pixels2 = IntArray(width)
        for (i in 0 until bitmap1.height) {
            bitmap1.getPixels(pixels1, 0, width, 0, i, width, 1)
            bitmap2.getPixels(pixels2, 0, width, 0, i, width, 1)
            if (!pixels1.contentEquals(pixels2)) {
                return false
            }
        }
        return true
    }
}
