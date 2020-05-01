package com.github.skgmn.mochka

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.github.skgmn.mochka.decoder.*
import com.github.skgmn.mochka.frame.FrameBuilder
import com.github.skgmn.mochka.source.*
import com.github.skgmn.mochka.util.LazyInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


abstract class BitmapDecoder {
    abstract val width: Int
    abstract val height: Int

    open fun scaleTo(width: Int, height: Int): BitmapDecoder {
        return ScaleToBitmapDecoder(
            this,
            width,
            height
        )
    }

    open fun scaleWidth(width: Int): BitmapDecoder {
        return ScaleWidthBitmapDecoder(
            this,
            width
        )
    }

    open fun scaleHeight(height: Int): BitmapDecoder {
        return ScaleHeightBitmapDecoder(
            this,
            height
        )
    }

    open fun scaleBy(scaleWidth: Float, scaleHeight: Float): BitmapDecoder {
        return if (scaleWidth == 1f && scaleHeight == 1f) {
            this
        } else {
            ScaleByBitmapDecoder(
                this,
                scaleWidth,
                scaleHeight
            )
        }
    }

    open fun region(left: Int, top: Int, right: Int, bottom: Int): BitmapDecoder {
        return RegionBitmapDecoder(
            this,
            left,
            top,
            right,
            bottom
        )
    }

    fun region(bounds: Rect): BitmapDecoder {
        return region(bounds.left, bounds.top, bounds.right, bounds.bottom)
    }

    @JvmOverloads
    fun frame(
        scaleType: ImageView.ScaleType,
        width: Int,
        height: Int,
        background: Drawable? = null
    ): Bitmap? {
        return FrameBuilder.newInstance(this, scaleType, width, height, background).decode()
    }

    fun decode(): Bitmap? {
        return decode(makeParameters(0))
    }

    internal abstract fun decode(parametersBuilder: DecodingParametersBuilder): Bitmap?

    internal open fun makeParameters(flags: Int): DecodingParametersBuilder {
        return DecodingParametersBuilder()
    }

    internal fun postProcess(bitmap: Bitmap?, params: DecodingParameters): Bitmap? {
        return if (bitmap == null || params.postScaleX == 1f && params.postScaleY == 1f) {
            bitmap
        } else {
            val m = Matrix()
            m.setScale(params.postScaleX, params.postScaleY)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
        }
    }

    companion object {
        private const val ASSET_PATH_PREFIX = "/android_asset/"

        fun fromAsset(context: Context, path: String): BitmapDecoder {
            return SourceBitmapDecoder(
                AssetBitmapSource(
                    context.assets,
                    path
                )
            )
        }

        fun fromAsset(assetManager: AssetManager, path: String): BitmapDecoder {
            return SourceBitmapDecoder(
                AssetBitmapSource(
                    assetManager,
                    path
                )
            )
        }

        @JvmStatic
        fun fromBitmap(bitmap: Bitmap): BitmapDecoder {
            return SourceBitmapDecoder(
                InMemoryBitmapSource(bitmap)
            )
        }

        @JvmStatic
        fun fromByteArray(array: ByteArray): BitmapDecoder {
            return fromByteArray(
                array,
                0,
                array.size
            )
        }

        @JvmStatic
        fun fromByteArray(array: ByteArray, offset: Int, length: Int): BitmapDecoder {
            return SourceBitmapDecoder(
                ByteArrayBitmapSource(
                    array,
                    offset,
                    length
                )
            )
        }

        @JvmStatic
        fun fromFile(file: File): BitmapDecoder {
            return SourceBitmapDecoder(
                FileBitmapSource(file)
            )
        }

        @JvmStatic
        fun fromResource(context: Context, @DrawableRes id: Int): BitmapDecoder {
            return SourceBitmapDecoder(
                ResourceBitmapSource(
                    context.resources,
                    id
                )
            )
        }

        @JvmStatic
        fun fromResource(res: Resources, @DrawableRes id: Int): BitmapDecoder {
            return SourceBitmapDecoder(
                ResourceBitmapSource(res, id)
            )
        }

        @JvmStatic
        fun fromStream(stream: InputStream): BitmapDecoder {
            return SourceBitmapDecoder(
                InputStreamBitmapSource(stream)
            )
        }

        @JvmStatic
        fun fromUri(context: Context, uri: Uri): BitmapDecoder {
            return when (val scheme = requireNotNull(uri.scheme)) {
                ContentResolver.SCHEME_ANDROID_RESOURCE -> {
                    val packageName: String = requireNotNull(uri.authority)
                    val res: Resources = if (context.packageName == packageName) {
                        context.resources
                    } else {
                        val pm = context.packageManager
                        try {
                            pm.getResourcesForApplication(packageName)
                        } catch (e: PackageManager.NameNotFoundException) {
                            throw IllegalArgumentException()
                        }
                    }
                    var id = 0
                    val segments: List<String> = requireNotNull(uri.pathSegments)
                    val size = segments.size
                    if (size == 2 && segments[0] == "drawable") {
                        val resName = segments[1]
                        id = res.getIdentifier(resName, "drawable", packageName)
                    } else if (size == 1 && TextUtils.isDigitsOnly(segments[0])) {
                        try {
                            id = segments[0].toInt()
                        } catch (ignored: NumberFormatException) {
                        }
                    }
                    if (id == 0) {
                        throw IllegalArgumentException()
                    } else {
                        fromResource(
                            res,
                            id
                        )
                    }
                }
                ContentResolver.SCHEME_FILE -> {
                    val path = requireNotNull(uri.path)
                    if (path.startsWith(ASSET_PATH_PREFIX)) {
                        fromAsset(
                            context.assets,
                            path.substring(ASSET_PATH_PREFIX.length)
                        )
                    } else {
                        fromFile(
                            File(path)
                        )
                    }
                }
                "http", "https", "ftp" -> {
                    fromStream(
                        LazyInputStream {
                            try {
                                URL(uri.toString()).openStream()
                            } catch (e: MalformedURLException) {
                                throw IllegalArgumentException(e)
                            }
                        })
                }
                ContentResolver.SCHEME_CONTENT -> {
                    val cr = context.contentResolver
                    fromStream(
                        LazyInputStream {
                            cr.openInputStream(uri) ?: throw FileNotFoundException()
                        })
                }
                else -> throw IllegalArgumentException("Unsupported scheme: $scheme")
            }
        }
    }
}
