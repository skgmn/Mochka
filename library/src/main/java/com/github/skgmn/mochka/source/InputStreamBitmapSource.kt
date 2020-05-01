package com.github.skgmn.mochka.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.skgmn.mochka.BitmapSource
import com.github.skgmn.mochka.util.RewindableInputStream
import java.io.InputStream

internal class InputStreamBitmapSource(
    inputStream: InputStream
) : BitmapSource() {
    private val rewindableInputStream =
        RewindableInputStream(inputStream)

    override val manualDensityScalingForRegional: Boolean
        get() = false

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeStream(rewindableInputStream, null, options)
    }

    override fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap? {
        val regionDecoder = BitmapRegionDecoder.newInstance(rewindableInputStream, false)
        return regionDecoder.decodeRegion(region, options)
    }

    override fun onDecodeStarted() {
        rewindableInputStream.rewind()
    }

    override fun onDecodeFinished() {
        rewindableInputStream.use {}
    }
}
