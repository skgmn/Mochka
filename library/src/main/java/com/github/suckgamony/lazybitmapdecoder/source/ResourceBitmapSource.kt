package com.github.suckgamony.lazybitmapdecoder.source

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import androidx.annotation.DrawableRes
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecoderState
import com.github.suckgamony.lazybitmapdecoder.util.InputStreamDecoderState
import com.github.suckgamony.lazybitmapdecoder.util.RewindableInputStream

internal class ResourceBitmapSource(
    private val res: Resources,
    @param:DrawableRes private val id: Int
) : BitmapSource() {
    override val densityScalingSupported: Boolean
        get() = true

    override fun decodeBitmap(state: DecoderState, options: BitmapFactory.Options): Bitmap? {
        return BitmapFactory.decodeResource(res, id, options)
    }

    override fun decodeBitmapRegion(state: DecoderState, region: Rect, options: BitmapFactory.Options): Bitmap? {
        state as InputStreamDecoderState
        val regionDecoder = BitmapRegionDecoder.newInstance(state.inputStream, false)
        return regionDecoder.decodeRegion(region, options)
    }

    @SuppressLint("ResourceType")
    override fun createRegionalState(): DecoderState {
        val inputStream = res.openRawResource(id)
        return InputStreamDecoderState(RewindableInputStream(inputStream))
    }
}
