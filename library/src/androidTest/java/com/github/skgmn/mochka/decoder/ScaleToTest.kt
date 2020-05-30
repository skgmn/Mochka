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
class ScaleToTest : InstrumentedTestBase() {
    @Test
    fun simple() {
        val optsDecodeBounds = BitmapFactory.Options()
        optsDecodeBounds.inJustDecodeBounds = true
        BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, optsDecodeBounds)

        val targetWidth = optsDecodeBounds.outWidth / 3
        val targetHeight = optsDecodeBounds.outHeight / 3

        val opts = BitmapFactory.Options()
        opts.inSampleSize = 2
        val byFactory = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, opts),
            targetWidth, targetHeight, true
        )

        val source = ResourceBitmapSource(
            appContext.resources,
            R.drawable.nodpi_image
        )
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(targetWidth, targetHeight)
        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertSimilar(byDecoder, byFactory)
    }
}
