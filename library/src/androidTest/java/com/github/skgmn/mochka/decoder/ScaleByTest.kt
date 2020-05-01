package com.github.skgmn.mochka.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.skgmn.mochka.InstrumentedTestBase
import com.github.skgmn.mochka.test.R
import com.github.skgmn.mochka.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

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

        val source = ResourceBitmapSource(
            appContext.resources,
            R.drawable.nodpi_image
        )
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.3f, 0.3f)
        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertSimilar(byDecoder, byFactory)
    }
}
