package com.github.skgmn.mochka.sampleapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.skgmn.mochka.Mochka
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LargeImageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_large_image, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val view = requireView()
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val imageDescView = view.findViewById<TextView>(R.id.imageDesc)

        val res = resources
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val width = getWidth(imageView)
            val decodeResult = withContext(Dispatchers.IO) {
                val decoder = Mochka.decodeResource(res, R.drawable.very_large_image)
                BitmapDecodeResult(
                    decoder.width,
                    decoder.height,
                    decoder.scaleWidth(width).decode()!!
                )
            }
            imageDescView.text = "Loaded ${decodeResult.width}x${decodeResult.height} bitmap"
            imageView.setImageBitmap(decodeResult.bitmap)
        }
    }

    private suspend fun getWidth(view: View): Int {
        return suspendCoroutine { cont ->
            view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
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
                    view.removeOnLayoutChangeListener(this)
                    cont.resume(right - left)
                }
            })
        }
    }

    private class BitmapDecodeResult(
        val width: Int,
        val height: Int,
        val bitmap: Bitmap
    )
}
