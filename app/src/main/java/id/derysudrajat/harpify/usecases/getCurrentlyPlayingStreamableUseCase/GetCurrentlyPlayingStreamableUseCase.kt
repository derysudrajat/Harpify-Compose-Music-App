package id.derysudrajat.harpify.usecases.getCurrentlyPlayingStreamableUseCase

import id.derysudrajat.harpify.domain.Streamable
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingStreamableUseCase {
    val currentlyPlayingStreamableStream: Flow<Streamable>
}