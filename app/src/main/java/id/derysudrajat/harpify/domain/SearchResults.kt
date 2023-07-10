package id.derysudrajat.harpify.domain

import id.derysudrajat.harpify.domain.SearchResult.*

data class SearchResults(
    val tracks: List<TrackSearchResult>
)