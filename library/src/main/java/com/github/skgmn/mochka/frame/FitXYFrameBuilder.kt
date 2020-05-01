package com.github.skgmn.mochka.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.skgmn.mochka.BitmapDecoder

internal class FitXYFrameBuilder(
    decoder: BitmapDecoder,
    frameWidth: Int,
    frameHeight: Int,
    background: Drawable?
) : FrameBuilder(decoder, frameWidth, frameHeight, background) {
    override fun getBounds(
        decoder: BitmapDecoder,
        frameWidth: Int,
        frameHeight: Int,
        outSrc: Rect?,
        outDest: Rect?
    ) {
        outSrc?.set(0, 0, decoder.width, decoder.height)
        outDest?.set(0, 0, frameWidth, frameHeight)
    }
}
