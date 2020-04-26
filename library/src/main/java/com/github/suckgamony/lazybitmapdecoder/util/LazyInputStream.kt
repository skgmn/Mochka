package com.github.suckgamony.lazybitmapdecoder.util

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

internal class LazyInputStream(
    private val opener: () -> InputStream
) : InputStream() {
    @Volatile
    private var inputStream: InputStream? = null

    private val stream: InputStream
        get() {
            return inputStream ?: synchronized(opener) {
                inputStream ?: opener().also { inputStream = it }
            }
        }

    @Throws(IOException::class)
    override fun read(): Int {
        return stream.read()
    }

    @Throws(IOException::class)
    override fun available(): Int {
        return stream.available()
    }

    @Throws(IOException::class)
    override fun close() {
        inputStream?.close()
    }

    override fun mark(readlimit: Int) {
        stream.mark(readlimit)
    }

    override fun markSupported(): Boolean {
        return stream.markSupported()
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray): Int {
        return stream.read(buffer)
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, byteOffset: Int, byteCount: Int): Int {
        return stream.read(buffer, byteOffset, byteCount)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun reset() {
        stream.reset()
    }

    @Throws(IOException::class)
    override fun skip(byteCount: Long): Long {
        return stream.skip(byteCount)
    }
}