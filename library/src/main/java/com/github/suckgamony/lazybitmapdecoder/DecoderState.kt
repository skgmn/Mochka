package com.github.suckgamony.lazybitmapdecoder

import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

internal open class DecoderState {
    private val decoded = AtomicBoolean(false)

    open fun startDecode() {
        if (decoded.getAndSet(true)) {
            throw IllegalStateException()
        }
    }
}
