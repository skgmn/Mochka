package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.test.R
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultBitmapDecoderTest : InstrumentedTestBase() {
    @Test
    fun decodeResource() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val byFactory = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = DefaultBitmapDecoder(source)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeResourceWithMetadata() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val byFactory = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = DefaultBitmapDecoder(source)

        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeResourceMultipleDensities() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val ids = intArrayOf(
            R.drawable.xxxhdpi_image,
            R.drawable.xxhdpi_image,
            R.drawable.xhdpi_image,
            R.drawable.hdpi_image,
            R.drawable.mdpi_image
        )

        ids.forEach {
            val byFactory = BitmapFactory.decodeResource(appContext.resources, it)

            val source = ResourceBitmapSource(appContext.resources, it)
            val decoder = DefaultBitmapDecoder(source)
            val byDecoder = assertNotNull(decoder.decode())

            assertEquals(byDecoder, byFactory)
        }
    }
}
