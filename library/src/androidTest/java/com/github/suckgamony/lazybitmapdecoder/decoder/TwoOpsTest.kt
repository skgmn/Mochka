package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class TwoOpsTest : InstrumentedTestBase() {
    @Test
    fun scaleToScaleTo() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledTo = Bitmap.createScaledBitmap(bitmap, 100, 200, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .scaleTo(100, 200)
        assertEquals(100, decoder.width)
        assertEquals(200, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleToScaleBy() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledTo = Bitmap.createScaledBitmap(bitmap, 110, 240, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleBy(1.1f, 1.2f)
        assertEquals(110, decoder.width)
        assertEquals(240, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE)
    }

    @Test
    fun scaleToRegion() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledTo = Bitmap.createScaledBitmap(bitmap, 300, 400, true)
        val regioned = Bitmap.createBitmap(scaledTo, 100, 110, 120, 130)
        val byFactory = regioned

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(300, 400)
            .region(100, 110, 100 + 120, 110 + 130)
        assertEquals(120, decoder.width)
        assertEquals(130, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE)
    }

    @Test
    fun scaleByScaleTo() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledTo = Bitmap.createScaledBitmap(bitmap, 200, 210, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.7f, 0.8f)
            .scaleTo(200, 210)
        assertEquals(200, decoder.width)
        assertEquals(210, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun scaleByScaleBy() {
        val m = Matrix()
        val opts = BitmapFactory.Options()
        opts.inSampleSize = 2
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, opts)
        m.setScale(0.35f * opts.inSampleSize, 0.48f * opts.inSampleSize)
        val byFactory = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, m, true
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.5f, 0.6f)
            .scaleBy(0.7f, 0.8f)
        assertEquals(byFactory.width, decoder.width)
        assertEquals(byFactory.height, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE)
    }

    @Test
    fun scaleByRegion() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledBy = Matrix().let { m ->
            m.setScale(0.7f, 0.8f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
        val regioned = Bitmap.createBitmap(scaledBy, 100, 110, 120, 130)
        val byFactory = regioned

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.7f, 0.8f)
            .region(100, 110, 100 + 120, 110 + 130)
        assertEquals(120, decoder.width)
        assertEquals(130, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }

    @Test
    fun regionScaleTo() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val regioned = Bitmap.createBitmap(bitmap, 100, 110, 120, 130)
        val scaledTo = Bitmap.createScaledBitmap(regioned, 140, 150, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .region(100, 110, 100 + 120, 110 + 130)
            .scaleTo(140, 150)
        assertEquals(140, decoder.width)
        assertEquals(150, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }
}
