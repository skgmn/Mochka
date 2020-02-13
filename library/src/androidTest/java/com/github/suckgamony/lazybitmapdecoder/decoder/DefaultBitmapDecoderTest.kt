package com.github.suckgamony.lazybitmapdecoder.decoder

import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.test.R
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultBitmapDecoderTest : InstrumentedTestBase() {
    @Test
    fun decodeResource() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val source = ResourceBitmapSource(appContext.resources, R.drawable.image1)
        val decoder = DefaultBitmapDecoder(source)
        val bitmap1 = assertNotNull(decoder.decode())
        val bitmap2 = BitmapFactory.decodeResource(appContext.resources, R.drawable.image1)

        assertEquals(bitmap1, bitmap2)
    }
}
