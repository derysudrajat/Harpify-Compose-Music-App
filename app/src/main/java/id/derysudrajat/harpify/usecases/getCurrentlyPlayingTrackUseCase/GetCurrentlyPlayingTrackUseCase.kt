package id.derysudrajat.harpify.usecases.getCurrentlyPlayingTrackUseCase

import id.derysudrajat.harpify.domain.SearchResult
import kotlinx.coroutines.flow.Flow

interface GetCurrentlyPlayingTrackUseCase {
    val currentlyPlayingTrackStream:Flow<SearchResult.TrackSearchResult?>
}