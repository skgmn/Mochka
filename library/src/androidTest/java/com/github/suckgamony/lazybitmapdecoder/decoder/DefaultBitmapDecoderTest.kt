package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ByteArrayBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.FileBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(AndroidJUnit4::class)
class DefaultBitmapDecoderTest : InstrumentedTestBase() {
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun decodeResource() {
        val byFactory = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = DefaultBitmapDecoder(source)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactory)
    }

    @Test
    fun decodeResourceWithMetadata() {
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

    @Test
    fun decodeByteArray() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        val data = ByteArrayOutputStream().use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }

        val byFactoryFromByteArray = BitmapFactory.decodeByteArray(data, 0, data.size)

        val source = ByteArrayBitmapSource(data, 0, data.size)
        val decoder = DefaultBitmapDecoder(source)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactoryFromByteArray)
    }

    @Test
    fun decodeByteArrayWithMetadata() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        val data = ByteArrayOutputStream().use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }

        val byFactoryFromByteArray = BitmapFactory.decodeByteArray(data, 0, data.size)

        val source = ByteArrayBitmapSource(data, 0, data.size)
        val decoder = DefaultBitmapDecoder(source)
        assertEquals(decoder.width, byFactoryFromByteArray.width)
        assertEquals(decoder.height, byFactoryFromByteArray.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactoryFromByteArray)
    }

    @Test
    fun decodeFile() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        appContext.openFileOutput("decodeFileTest.png", Context.MODE_PRIVATE).use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val file = File(appContext.filesDir, "decodeFileTest.png")
        val byFactoryFromFile = BitmapFactory.decodeFile(file.path)

        val source = FileBitmapSource(file)
        val decoder = DefaultBitmapDecoder(source)
        val byDecoder = assertNotNull(decoder.decode())

        assertEquals(byDecoder, byFactoryFromFile)
    }

    @Test
    fun decodeFileWithMetadata() {
        val byFactoryFromRes = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image)
        appContext.openFileOutput("decodeFileTest.png", Context.MODE_PRIVATE).use {
            byFactoryFromRes.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        val file = File(appContext.filesDir, "decodeFileTest.png")
        val byFactoryFromFile = BitmapFactory.decodeFile(file.path)

        val source = FileBitmapSource(file)
        val decoder = DefaultBitmapDecoder(source)
        assertEquals(decoder.width, byFactoryFromFile.width)
        assertEquals(decoder.height, byFactoryFromFile.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactoryFromFile)
    }
}
