package com.github.suckgamony.lazybitmapdecoder.decoder

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.suckgamony.lazybitmapdecoder.InstrumentedTestBase
import com.github.suckgamony.lazybitmapdecoder.source.ResourceBitmapSource
import com.github.suckgamony.lazybitmapdecoder.test.R
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScaleByBitmapDecoderTest : InstrumentedTestBase() {
    @Test
    fun scaleToScaleByScaleTo() {
        val source = ResourceBitmapSource(appContext.resources, R.drawable.nodpi_image)
        val decoder = SourceBitmapDecoder(source)
            .scaleTo(100, 200)
            .scaleBy(0.7f, 0.7f)
            .scaleTo(300, 400)

        Assert.assertTrue(decoder is ScaleToBitmapDecoder)
        decoder as ScaleToBitmapDecoder

        Assert.assertTrue(decoder.other is SourceBitmapDecoder && decoder.other.source == source)
        Assert.assertEquals(300, decoder.width)
        Assert.assertEquals(400, decoder.height)
    }
}
