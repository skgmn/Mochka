package com.github.skgmn.mochka.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.skgmn.mochka.BitmapSource
import java.io.File

internal class FileBitmapSource(
    private val file: File
) : BitmapSource() {
    override val manualDensityScalingForRegional: Boolean
        get() = false

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeFile(file.path, options)
    }

    override fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap? {
        val regionDecoder = BitmapRegionDecoder.newInstance(file.path, false)
        return regionDecoder.decodeRegion(region, options)
    }
}
