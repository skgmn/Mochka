package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ByteArrayBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.FileBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.InputStreamBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.File

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
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
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
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)

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
            val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
            val byDecoder = assertNotNull(decoder.decode())

            assertEquals(byDecoder, byFactory, TOLERANCE)
        }
    }

    @Test
    fun decodeByteArray() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        val data = ByteArrayOutputStream().use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }

        val byFactoryFromByteArray = Bitmap.createBitmap(
            BitmapFactory.decodeByteArray(data, 0, data.size),
            12, 34, 56, 78
        )

        val source = ByteArrayBitmapSource(data, 0, data.size)
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactoryFromByteArray, TOLERANCE)
    }

    @Test
    fun decodeByteArrayWithMetadata() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        val data = ByteArrayOutputStream().use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }

        val byFactoryFromByteArray = Bitmap.createBitmap(
            BitmapFactory.decodeByteArray(data, 0, data.size),
            12, 34, 56, 78
        )

        val source = ByteArrayBitmapSource(data, 0, data.size)
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
        assertEquals(decoder.width, byFactoryFromByteArray.width)
        assertEquals(decoder.height, byFactoryFromByteArray.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactoryFromByteArray, TOLERANCE)
    }

    @Test
    fun decodeFile() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        appContext.openFileOutput("decodeFileTest.png", Context.MODE_PRIVATE).use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val file = File(appContext.filesDir, "decodeFileTest.png")
        val byFactoryFromFile = Bitmap.createBitmap(
            BitmapFactory.decodeFile(file.path),
            12, 34, 56, 78
        )

        val source = FileBitmapSource(file)
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactoryFromFile, TOLERANCE)
    }

    @Test
    fun decodeFileWithMetadata() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        appContext.openFileOutput("decodeFileTest.png", Context.MODE_PRIVATE).use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val file = File(appContext.filesDir, "decodeFileTest.png")
        val byFactoryFromFile = Bitmap.createBitmap(
            BitmapFactory.decodeFile(file.path),
            12, 34, 56, 78
        )

        val source = FileBitmapSource(file)
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
        assertEquals(decoder.width, byFactoryFromFile.width)
        assertEquals(decoder.height, byFactoryFromFile.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactoryFromFile, TOLERANCE)
    }

    @Test
    fun decodeStream() {
        val byFactory = Bitmap.createBitmap(
            BitmapFactory.decodeStream(appContext.resources.openRawResource(R.drawable.nodpi_image)),
            12, 34, 56, 78
        )

        val source = InputStreamBitmapSource(appContext.resources.openRawResource(R.drawable.nodpi_image))
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeStreamWithMetadata() {
        val byFactory = Bitmap.createBitmap(
            BitmapFactory.decodeStream(appContext.resources.openRawResource(R.drawable.nodpi_image)),
            12, 34, 56, 78
        )

        val source = InputStreamBitmapSource(appContext.resources.openRawResource(R.drawable.nodpi_image))
        val decoder = RegionalSourceBitmapDecoder(source, 12, 34, 12 + 56, 34 + 78)

        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }

    companion object {
        private const val TOLERANCE = 0x0c
    }
}
