package com.github.suckgamony.lazybitmapdecoder.frame

import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.github.suckgamony.lazybitmapdecoder.BitmapDecoder
import com.github.suckgamony.lazybitmapdecoder.util.AspectRatioCalculator

internal class CenterCropFrameBuilder(
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
        val targetWidth: Int
        var targetHeight: Int = AspectRatioCalculator.getHeight(width, height, frameWidth)
        if (targetHeight >= frameHeight) {
            targetWidth = frameWidth
        } else {
            targetWidth = AspectRatioCalculator.getWidth(width, height, frameHeight)
            targetHeight = frameHeight
        }
        val targetLeft = (frameWidth - targetWidth) / 2
        val targetTop = (frameHeight - targetHeight) / 2
        val ratioWidth = targetWidth.toFloat() / width
        val ratioHeight = targetHeight.toFloat() / height
        if (outSrc != null) {
            outSrc.left = Math.round(-targetLeft / ratioWidth)
            outSrc.top = Math.round(-targetTop / ratioHeight)
            outSrc.right = outSrc.left + Math.round(frameWidth / ratioWidth)
            outSrc.bottom = outSrc.top + Math.round(frameHeight / ratioHeight)
        }
        outDest?.set(0, 0, frameWidth, frameHeight)
    }
}
