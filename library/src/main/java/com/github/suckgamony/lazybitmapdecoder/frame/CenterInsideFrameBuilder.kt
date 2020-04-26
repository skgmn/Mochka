package com.github.suckgamony.lazybitmapdecoder.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.util.AspectRatioCalculator

internal class CenterInsideFrameBuilder(
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
        outSrc?.set(0, 0, width, height)
        if (outDest != null) {
            if (width <= frameWidth && height <= frameHeight) {
                outDest.left = (frameWidth - width) / 2
                outDest.top = (frameHeight - height) / 2
                outDest.right = outDest.left + width
                outDest.bottom = outDest.top + height
            } else {
                val targetWidth: Int
                var targetHeight: Int = AspectRatioCalculator.getHeight(width, height, frameWidth)
                if (targetHeight <= frameHeight) {
                    targetWidth = frameWidth
                } else {
                    targetWidth = AspectRatioCalculator.getWidth(width, height, frameHeight)
                    targetHeight = frameHeight
                }
                outDest.left = (frameWidth - targetWidth) / 2
                outDest.top = (frameHeight - targetHeight) / 2
                outDest.right = outDest.left + targetWidth
                outDest.bottom = outDest.top + targetHeight
            }
        }
    }
}
