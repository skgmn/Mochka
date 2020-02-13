package com.github.suckgamony.lazybitmapdecoder.source

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import androidx.annotation.DrawableRes
import com.github.suckgamony.lazybitmapdecoder.BitmapSource

class ResourceBitmapSource(
    private val res: Resources,
    @param:DrawableRes private val id: Int
) : BitmapSource {
    override val densityScalingSupported: Boolean
        get() = true

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeResource(res, id, options)
    }

    @SuppressLint("ResourceType")
    override fun decodeBitmapRegion(rect: Rect, options: BitmapFactory.Options): Bitmap? {
        val inputStream = res.openRawResource(id)
        val regionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
        return regionDecoder.decodeRegion(rect, options)
    }
}
