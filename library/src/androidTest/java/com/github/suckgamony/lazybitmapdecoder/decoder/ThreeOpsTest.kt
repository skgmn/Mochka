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
    fun scaleByRegionScaleTo() {
        val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.nodpi_image, null)
        val scaledBy = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * 0.9f).roundToInt(),
            (bitmap.height * 0.8f).roundToInt(),
            true
        )
        val regioned = Bitmap.createBitmap(scaledBy, 100, 110, 120, 130)
        val scaledTo = Bitmap.createScaledBitmap(regioned, 140, 150, true)
        val byFactory = scaledTo

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleBy(0.9f, 0.8f)
            .region(100, 110, 100 + 120, 110 + 130)
            .scaleTo(140,150)
        assertEquals(140, decoder.width)
        assertEquals(150, decoder.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory, MAX_TOLERENCE, 0.9f)
    }
}
