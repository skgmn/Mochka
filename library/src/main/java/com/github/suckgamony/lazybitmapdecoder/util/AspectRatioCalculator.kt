package com.github.suckgamony.lazybitmapdecoder.util

import kotlin.math.roundToInt

object AspectRatioCalculator {
    fun getHeight(width: Int, height: Int, targetWidth: Int): Int {
        val ratio = height.toDouble() / width
        return (ratio * targetWidth).roundToInt()
    }

    fun getWidth(width: Int, height: Int, targetHeight: Int): Int {
        val ratio = width.toDouble() / height
        return (ratio * targetHeight).roundToInt()
    }

    fun getHeight(width: Float, height: Float, targetWidth: Float): Float {
        return height / width * targetWidth
    }

    fun getWidth(width: Float, height: Float, targetHeight: Float): Float {
        return width / height * targetHeight
    }
}
