package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.test.R
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.roundToInt

@RunWith(AndroidJUnit4::class)
class ScaleByTest : InstrumentedTestBase() {
    @Test
    fun scaleBy() {
        val opts = BitmapFactory.Options()
        opts.inSampleSize = 2
        val m = Matrix()
        m.setScale(0.6f, 0.6f)
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, opts)
        val byFactory = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, m, true
        )

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source).scaleBy(0.3f, 0.3f)
        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }
}
