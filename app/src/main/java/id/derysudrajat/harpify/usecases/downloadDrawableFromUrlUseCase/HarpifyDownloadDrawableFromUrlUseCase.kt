package id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase

import android.content.Context
import android.graphics.drawable.Drawable
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import id.derysudrajat.harpify.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HarpifyDownloadDrawableFromUrlUseCase @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : DownloadDrawableFromUrlUseCase {

    override suspend fun invoke(
        urlString: String, context: Context
    ): Result<Drawable> = withContext(ioDispatcher) {
        val imageRequest = ImageRequest.Builder(context)
            .data(urlString)
            .allowHardware(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
        when (val imageResult = context.imageLoader.execute(imageRequest)) {
            is SuccessResult -> Result.success(imageResult.drawable)
            is ErrorResult -> Result.failure(imageResult.throwable)
        }
    }

}