package id.derysudrajat.harpify.data.remote.response

import id.derysudrajat.harpify.domain.SearchResults
import com.fasterxml.jackson.annotation.JsonProperty

data class SearchResultsResponse(
    val tracks: Tracks?
) {
    data class Tracks(@JsonProperty("items") val value: List<TrackResponseWithAlbumMetadata>)
}

fun SearchResultsResponse.toSearchResults() = SearchResults(
    tracks = tracks?.value?.map { it.toTrackSearchResult() } ?: emptyList()
)

