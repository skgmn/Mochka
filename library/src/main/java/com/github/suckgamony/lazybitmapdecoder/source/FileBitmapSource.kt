package com.github.suckgamony.lazybitmapdecoder.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import java.io.File

internal class FileBitmapSource(
    private val file: File
) : BitmapSource() {
    override val densityScalingSupported: Boolean
        get() = false

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeFile(file.path, options)
    }

    override fun decodeBitmapRegion(rect: Rect, options: BitmapFactory.Options): Bitmap? {
        val regionDecoder = BitmapRegionDecoder.newInstance(file.path, false)
        return regionDecoder.decodeRegion(rect, options)
    }
}
