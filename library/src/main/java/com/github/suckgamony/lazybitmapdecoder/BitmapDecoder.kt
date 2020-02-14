package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap

abstract class BitmapDecoder(
    protected val source: BitmapSource
) {
    abstract val width: Int
    abstract val height: Int

    abstract fun decode(decodingOptions: DecodingOptions = DecodingOptions()): Bitmap?
}
