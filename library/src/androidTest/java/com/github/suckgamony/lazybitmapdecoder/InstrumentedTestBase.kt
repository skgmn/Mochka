package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

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
        for (i in 0 until bitmap1.height) {
            for (j in 0 until bitmap1.width) {
                if (bitmap1.getPixel(j, i) != bitmap2.getPixel(j, i)) {
                    return false
                }
            }
        }
        return true
    }
}
