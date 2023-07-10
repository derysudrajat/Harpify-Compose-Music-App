package id.derysudrajat.harpify.usecases.getPlaybackLoadingStatusUseCase

import kotlinx.coroutines.flow.Flow

interface GetPlaybackLoadingStatusUseCase {
    val loadingStatusStream: Flow<Boolean>
}