package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ByteArrayBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.FileBitmapSource
import com.github.suckgamony.lazybitmapdecoder.source.InputStreamBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(AndroidJUnit4::class)
class ScaleToTest : InstrumentedTestBase() {
    @Test
    fun decodeResource() {
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

        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source).scaleTo(targetWidth, targetHeight)
        assertEquals(decoder.width, byFactory.width)
        assertEquals(decoder.height, byFactory.height)

        val byDecoder = assertNotNull(decoder.decode())
        assertEquals(byDecoder, byFactory)
    }
}
