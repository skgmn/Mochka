package com.github.skgmn.mochka.source

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.skgmn.mochka.BitmapSource

internal class AssetBitmapSource(
    private val assetManager: AssetManager,
    private val fileName: String
) : BitmapSource() {
    override val manualDensityScalingForRegional: Boolean
        get() = false

    override fun decodeBitmap(options: BitmapFactory.Options): Bitmap? {
        return assetManager.open(fileName).use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        }
    }

    override fun decodeBitmapRegion(region: Rect, options: BitmapFactory.Options): Bitmap? {
        return assetManager.open(fileName).use { stream ->
            val regionDecoder = BitmapRegionDecoder.newInstance(stream, false)
            regionDecoder.decodeRegion(region, options)
        }
    }
}
