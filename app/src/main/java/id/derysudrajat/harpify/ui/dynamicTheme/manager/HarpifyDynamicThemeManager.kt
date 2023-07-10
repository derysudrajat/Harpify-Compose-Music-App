package id.derysudrajat.harpify.ui.dynamicTheme.manager

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase.DownloadDrawableFromUrlUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HarpifyDynamicThemeManager(
    private val downloadDrawableFromUrlUseCase: DownloadDrawableFromUrlUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : DynamicThemeManager {
    private val colorCache = LruCache<String, Color>(10)
    private suspend fun getBackgroundColorForBitmap(bitmap: Bitmap): Color? =
        withContext(defaultDispatcher) {
            Palette.from(bitmap)
                .generate()
                .dominantSwatch
                ?.hsl
                ?.apply {
                    this[2] = 0.5f
                }
                ?.let(ColorUtils::HSLToColor)
                ?.let { Color(it) }
        }

    override suspend fun getBackgroundColorForImageFromUrl(url: String, context: Context): Color? {
        if (colorCache.get(url) != null) return colorCache.get(url)
        val bitmap = downloadDrawableFromUrlUseCase.invoke(url, context)
            .getOrNull()
            ?.toBitmap() ?: return null
        return getBackgroundColorForBitmap(bitmap).also { colorCache.put(url, it) }
    }
}

