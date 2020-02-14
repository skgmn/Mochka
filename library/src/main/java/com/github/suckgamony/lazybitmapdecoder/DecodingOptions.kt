package com.github.suckgamony.lazybitmapdecoder

import android.graphics.BitmapFactory

class DecodingOptions(
    private val mutable: Boolean = false
) {
    fun fillInOptions(options: BitmapFactory.Options) {
        options.inMutable = mutable
    }
}
