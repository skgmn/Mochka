package com.github.skgmn.mochka.sampleapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
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

class ScaleTypeFragment : Fragment() {
    private var sourceView: TextView? = null
    private var scaleMatrixView: TextView? = null
    private var scaleFitXYView: TextView? = null
    private var scaleFitStartView: TextView? = null
    private var scaleFitCenterView: TextView? = null
    private var scaleFitEndView: TextView? = null
    private var scaleCenterView: TextView? = null
    private var scaleCenterCropView: TextView? = null
    private var scaleCenterInsideView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scale_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sourceView = view.findViewById(R.id.source)
        scaleMatrixView = view.findViewById(R.id.scaleMatrix)
        scaleFitXYView = view.findViewById(R.id.scaleFitXY)
        scaleFitStartView = view.findViewById(R.id.scaleFitStart)
        scaleFitCenterView = view.findViewById(R.id.scaleFitCenter)
        scaleFitEndView = view.findViewById(R.id.scaleFitEnd)
        scaleCenterView = view.findViewById(R.id.scaleCenter)
        scaleCenterCropView = view.findViewById(R.id.scaleCenterCrop)
        scaleCenterInsideView = view.findViewById(R.id.scaleCenterInside)

        loadImages()
    }

    private fun loadImages() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = sourceView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .scaleWidth(width)
                    .decode()
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleMatrixView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.MATRIX, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleFitXYView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.FIT_XY, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleFitStartView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.FIT_START, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleFitCenterView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.FIT_CENTER, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleFitEndView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.FIT_END, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleCenterView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.CENTER, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleCenterCropView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.CENTER_CROP, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val textView = scaleCenterInsideView ?: return@launch
            val width = textView.getLayoutWidth()
            val bitmap = withContext(Dispatchers.IO) {
                Mochka.decodeResource(resources, R.drawable.image1)
                    .frame(
                        ImageView.ScaleType.CENTER_INSIDE, width, width, ColorDrawable(Color.GRAY)
                    )
            } ?: return@launch
            textView.setCompoundDrawablesWithIntrinsicBounds(
                null, BitmapDrawable(resources, bitmap), null, null
            )
        }
    }

    override fun onDestroyView() {
        scaleCenterInsideView = null
        scaleCenterCropView = null
        scaleCenterView = null
        scaleFitEndView = null
        scaleFitCenterView = null
        scaleFitStartView = null
        scaleFitXYView = null
        scaleMatrixView = null
        sourceView = null
        super.onDestroyView()
    }
}
