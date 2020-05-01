package com.github.skgmn.mochka

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.github.skgmn.mochka.decoder.SourceBitmapDecoder
import com.github.skgmn.mochka.source.*
import com.github.skgmn.mochka.util.LazyInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

object Mochka {
    private const val ASSET_PATH_PREFIX = "/android_asset/"

    fun decodeAsset(context: Context, path: String): BitmapDecoder {
        return SourceBitmapDecoder(AssetBitmapSource(context.assets, path))
    }

    fun decodeAsset(assetManager: AssetManager, path: String): BitmapDecoder {
        return SourceBitmapDecoder(AssetBitmapSource(assetManager, path))
    }

    @JvmStatic
    fun decodeByteArray(array: ByteArray): BitmapDecoder {
        return decodeByteArray(array, 0, array.size)
    }

    @JvmStatic
    fun decodeByteArray(array: ByteArray, offset: Int, length: Int): BitmapDecoder {
        return SourceBitmapDecoder(ByteArrayBitmapSource(array, offset, length))
    }

    @JvmStatic
    fun decodeFile(file: File): BitmapDecoder {
        return SourceBitmapDecoder(FileBitmapSource(file))
    }

    @JvmStatic
    fun decodeResource(context: Context, @DrawableRes id: Int): BitmapDecoder {
        return SourceBitmapDecoder(ResourceBitmapSource(context.resources, id))
    }

    @JvmStatic
    fun decodeResource(res: Resources, @DrawableRes id: Int): BitmapDecoder {
        return SourceBitmapDecoder(ResourceBitmapSource(res, id))
    }

    @JvmStatic
    fun fromStream(stream: InputStream): BitmapDecoder {
        return SourceBitmapDecoder(InputStreamBitmapSource(stream))
    }

    @JvmStatic
    fun decodeUri(context: Context, uri: Uri): BitmapDecoder {
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
                    decodeResource(res, id)
                }
            }
            ContentResolver.SCHEME_FILE -> {
                val path = requireNotNull(uri.path)
                if (path.startsWith(ASSET_PATH_PREFIX)) {
                    decodeAsset(context.assets, path.substring(ASSET_PATH_PREFIX.length))
                } else {
                    decodeFile(File(path))
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

    @JvmStatic
    fun transformBitmap(bitmap: Bitmap): BitmapDecoder {
        return SourceBitmapDecoder(InMemoryBitmapSource(bitmap))
    }
}
