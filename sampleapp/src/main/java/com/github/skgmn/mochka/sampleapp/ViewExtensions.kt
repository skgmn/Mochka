package com.github.skgmn.mochka.sampleapp

import android.view.View
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun View.getLayoutWidth(): Int {
    return suspendCoroutine { cont ->
        if (isLayoutRequested) {
            addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    removeOnLayoutChangeListener(this)
                    cont.resume(right - left)
                }
            })
        } else {
            cont.resume(width)
        }
    }
}
