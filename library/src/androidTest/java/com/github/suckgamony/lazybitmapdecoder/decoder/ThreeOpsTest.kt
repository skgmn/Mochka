package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class ThreeOpsTest : InstrumentedTestBase() {
    @Test
    fun scaleToScaleToScaleTo() {
        val scaledTo = decodeBitmapScaleTo(500, 600) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleTo(300, 400)
            .scaleTo(500, 600)
        assertEquals(500, decoder.width)
        assertEquals(600, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleToScaleToScaleBy() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val scaledBy = scaleBy(scaledTo, 0.9f, 0.8f)
        val byFactory = scaledBy

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleTo(300, 400)
            .scaleBy(0.9f, 0.8f)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }

    @Test
    fun scaleToScaleToRegion() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledTo, 110, 120, 130, 140)
        val byFactory = regioned

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleTo(300, 400)
            .region(110, 120, 110 + 130, 120 + 140)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE)
    }

    @Test
    fun scaleToScaleByScaleTo() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleBy(0.9f, 0.8f)
            .scaleTo(300, 400)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleToScaleByScaleBy() {
        val scaledTo = decodeBitmapScaleTo(63, 96) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleBy(0.9f, 0.8f)
            .scaleBy(0.7f, 0.6f)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleToScaleByRegion() {
        val scaledTo = decodeBitmapScaleTo(270, 320) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledTo, 100, 110, 120, 130)
        val byFactory = regioned

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .scaleBy(0.9f, 0.8f)
            .region(100, 110, 100 + 120, 110 + 130)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }

    @Test
    fun scaleToRegionScaleTo() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledTo, 100, 110, 120, 130)
        val scaledTo2 = Bitmap.createScaledBitmap(regioned, 100, 200, true)
        val byFactory = scaledTo2

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .region(100, 110, 100 + 120, 110 + 130)
            .scaleTo(100, 200)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }

    @Test
    fun scaleToRegionScaleBy() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledTo, 100, 110, 120, 130)
        val scaledBy = scaleBy(regioned, 0.9f, 0.8f)
        val byFactory = scaledBy

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .region(100, 110, 100 + 120, 110 + 130)
            .scaleBy(0.9f, 0.8f)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }

    @Test
    fun scaleToRegionRegion() {
        val scaledTo = decodeBitmapScaleTo(300, 400) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledTo, 100, 110, 120, 130)
        val regioned2 = Bitmap.createBitmap(regioned, 10, 20, 30, 40)
        val byFactory = regioned2

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .region(100, 110, 100 + 120, 110 + 130)
            .region(10, 20, 10 + 30, 20 + 40)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE)
    }

    @Test
    fun scaleByRegionScaleTo() {
        val scaledBy = decodeBitmapScaleBy(0.9f, 0.8f) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }
        val regioned = Bitmap.createBitmap(scaledBy, 100, 110, 120, 130)
        val scaledTo = Bitmap.createScaledBitmap(regioned, 140, 150, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.9f, 0.8f)
            .region(100, 110, 100 + 120, 110 + 130)
            .scaleTo(140, 150)
        assertEquals(140, decoder.width)
        assertEquals(150, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }
}
