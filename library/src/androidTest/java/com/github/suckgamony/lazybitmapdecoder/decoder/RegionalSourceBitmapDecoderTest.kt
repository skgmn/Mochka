package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionalSourceBitmapDecoderTest : InstrumentedTestBase() {
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun decodeResource() {
        val byFactory = Bitmap.createBitmap(
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image),
            12, 34, 56, 78
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = RegionalSourceBitmapDecoder(source, Rect(12, 34, 12 + 56, 34 + 78))
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeResourceWithMetadata() {
        val byFactory = Bitmap.createBitmap(
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image),
            12, 34, 56, 78
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = RegionalSourceBitmapDecoder(source, Rect(12, 34, 12 + 56, 34 + 78))

        Assert.assertEquals(decoder.width, byFactory.width)
        Assert.assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeResourceMultipleDensities() {
        val ids = intArrayOf(
            R.drawable.xxxhdpi_image,
            R.drawable.xxhdpi_image,
            R.drawable.xhdpi_image,
            R.drawable.hdpi_image,
            R.drawable.mdpi_image
        )

        ids.forEach {
            val byFactory = Bitmap.createBitmap(
                BitmapFactory.decodeResource(appContext.resources, it),
                12, 34, 56, 78
            )

            val source = ResourceBitmapSource(appContext.resources, it)
            val decoder = RegionalSourceBitmapDecoder(source, Rect(12, 34, 12 + 56, 34 + 78))
            val byDecoder = assertNotNull(decoder.decode())

            assertEquals(byDecoder, byFactory)
        }
    }
}
