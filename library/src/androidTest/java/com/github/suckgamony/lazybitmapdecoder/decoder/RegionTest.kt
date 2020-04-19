package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
class RegionTest : InstrumentedTestBase() {
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun region() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val byFactory = Bitmap.createBitmap(
            bitmap, 100, 110, 120, 130
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source).region(100, 110, 100 + 120, 110 + 130)
        Assert.assertEquals(decoder.width, byFactory.width)
        Assert.assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleByRegion() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap, bitmap.width * 2, bitmap.height * 3, true
        )
        val byFactory = Bitmap.createBitmap(
            scaledBitmap, 100, 110, 120, 130
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(2f, 3f)
            .region(100, 110, 100 + 120, 110 + 130)
        Assert.assertEquals(decoder.width, byFactory.width)
        Assert.assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }
}
