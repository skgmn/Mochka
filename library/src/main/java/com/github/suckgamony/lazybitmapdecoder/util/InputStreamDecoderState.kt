package com.github.suckgamony.lazybitmapdecoder.util

import com.github.suckgamony.lazybitmapdecoder.DecoderState

internal class InputStreamDecoderState(
    val inputStream: RewindableInputStream
) : DecoderState() {
    override fun startDecode() {
        super.startDecode()
        inputStream.rewind()
    }

    override fun finishDecode() {
        inputStream.use { }
        super.finishDecode()
    }
}
