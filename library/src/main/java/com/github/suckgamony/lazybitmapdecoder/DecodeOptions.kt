package com.github.suckgamony.lazybitmapdecoder

import android.graphics.BitmapFactory

class DecodeOptions(
    private val mutable: Boolean = false
) {
    fun toBitmapFactoryOptions(): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inMutable = mutable
        return options
    }
}
