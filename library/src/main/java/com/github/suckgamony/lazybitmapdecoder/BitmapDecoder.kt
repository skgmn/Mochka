package com.github.suckgamony.lazybitmapdecoder

import android.graphics.Bitmap

abstract class BitmapDecoder(
    protected val source: BitmapSource
) {
    abstract val width: Int
    abstract val height: Int

    abstract fun decode(options: DecodeOptions = DecodeOptions()): Bitmap?

    internal abstract fun decodeInternal(state: DecodeState, options: DecodeOptions): Bitmap?
}
