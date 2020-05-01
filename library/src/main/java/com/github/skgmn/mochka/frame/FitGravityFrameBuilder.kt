package com.github.skgmn.mochka.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.skgmn.mochka.BitmapDecoder
import com.github.skgmn.mochka.util.AspectRatioCalculator

internal class FitGravityFrameBuilder(
    decoder: BitmapDecoder,
    frameWidth: Int,
    frameHeight: Int,
    background: Drawable?,
    private val gravity: Int
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
            val targetWidth: Int
            var targetHeight: Int = AspectRatioCalculator.getHeight(width, height, frameWidth)
            if (targetHeight <= frameHeight) {
                targetWidth = frameWidth
            } else {
                targetWidth = AspectRatioCalculator.getWidth(width, height, frameHeight)
                targetHeight = frameHeight
            }
            when (gravity) {
                GRAVITY_START -> outDest.set(0, 0, targetWidth, targetHeight)
                GRAVITY_CENTER -> {
                    outDest.left = (frameWidth - targetWidth) / 2
                    outDest.top = (frameHeight - targetHeight) / 2
                    outDest.right = outDest.left + targetWidth
                    outDest.bottom = outDest.top + targetHeight
                }
                GRAVITY_END -> {
                    outDest.right = frameWidth
                    outDest.bottom = frameHeight
                    outDest.left = outDest.right - targetWidth
                    outDest.top = outDest.bottom - targetHeight
                }
                else -> outDest[0, 0, targetWidth] = targetHeight
            }
        }
    }

    companion object {
        const val GRAVITY_START = 0
        const val GRAVITY_CENTER = 1
        const val GRAVITY_END = 2
    }
}