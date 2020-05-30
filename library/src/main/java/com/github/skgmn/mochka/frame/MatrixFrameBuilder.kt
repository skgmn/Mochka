package com.github.skgmn.mochka.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.skgmn.mochka.BitmapDecoder

internal class MatrixFrameBuilder(
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
        val width = Math.min(decoder.width, frameWidth)
        val height = Math.min(decoder.height, frameHeight)
        outSrc?.set(0, 0, width, height)
        outDest?.set(0, 0, width, height)
    }
}
