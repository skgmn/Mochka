package com.github.suckgamony.lazybitmapdecoder.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapSource

internal class ByteArrayBitmapSource(
    private val data: ByteArray,
    private val offset: Int,
    private val length: Int
) : BitmapSource() {
    override val manualDensityScalingForRegional: Boolean
        get() = false

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeByteArray(data, offset, length, options)
    }

    override fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap? {
        val regionDecoder = BitmapRegionDecoder.newInstance(data, offset, length, false)
        return regionDecoder.decodeRegion(region, options)
    }
}
