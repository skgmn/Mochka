package com.github.suckgamony.lazybitmapdecoder.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import com.github.suckgamony.lazybitmapdecoder.BitmapSource
import com.github.suckgamony.lazybitmapdecoder.DecoderState
import com.github.suckgamony.lazybitmapdecoder.util.InputStreamDecoderState
import com.github.suckgamony.lazybitmapdecoder.util.RewindableInputStream
import java.io.InputStream

internal class InputStreamBitmapSource(
    private val inputStreamSupplier: () -> InputStream
) : BitmapSource() {
    override val densityScalingSupported: Boolean
        get() = false

    override fun decodeBitmap(state: DecoderState, options: BitmapFactory.Options): Bitmap? {
        state as InputStreamDecoderState
        return BitmapFactory.decodeStream(state.inputStream, null, options)
    }

    override fun decodeBitmapRegion(state: DecoderState, region: Rect, options: BitmapFactory.Options): Bitmap? {
        state as InputStreamDecoderState
        val regionDecoder = BitmapRegionDecoder.newInstance(state.inputStream, false)
        return regionDecoder.decodeRegion(region, options)
    }

    override fun createState(): DecoderState {
        return InputStreamDecoderState(RewindableInputStream(inputStreamSupplier()))
    }
}
