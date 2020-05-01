package com.github.skgmn.mochka.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.skgmn.mochka.BitmapDecoder

internal class CenterFrameBuilder(
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
        val width: Int = decoder.width
        val height: Int = decoder.height
        if (width > frameWidth) {
            if (outSrc != null) {
                outSrc.left = (width - frameWidth) / 2
                outSrc.right = outSrc.left + frameWidth
            }
            if (outDest != null) {
                outDest.left = 0
                outDest.right = frameWidth
            }
        } else {
            if (outSrc != null) {
                outSrc.left = 0
                outSrc.right = width
            }
            if (outDest != null) {
                outDest.left = (frameWidth - width) / 2
                outDest.right = outDest.left + width
            }
        }
        if (height > frameHeight) {
            if (outSrc != null) {
                outSrc.top = (height - frameHeight) / 2
                outSrc.bottom = outSrc.top + frameHeight
            }
            if (outDest != null) {
                outDest.top = 0
                outDest.bottom = frameHeight
            }
        } else {
            if (outSrc != null) {
                outSrc.top = 0
                outSrc.bottom = height
            }
            if (outDest != null) {
                outDest.top = (frameHeight - height) / 2
                outDest.bottom = outDest.top + height
            }
        }
    }
}