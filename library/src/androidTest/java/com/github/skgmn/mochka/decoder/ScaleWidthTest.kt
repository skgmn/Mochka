package com.github.skgmn.mochka.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.skgmn.mochka.InstrumentedTestBase
import com.github.skgmn.mochka.test.R
import com.github.skgmn.mochka.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScaleWidthTest : InstrumentedTestBase() {
    @Test
    fun simple() {
        val byFactory = decodeBitmapScaleTo(400, 240) {
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, it)
        }

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleWidth(400)
        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertSimilar(byDecoder, byFactory)
    }
}
