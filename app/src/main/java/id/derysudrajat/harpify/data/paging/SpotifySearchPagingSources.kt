package id.derysudrajat.harpify.data.paging

import id.derysudrajat.harpify.data.remote.musicservice.SearchQueryType
import id.derysudrajat.harpify.data.remote.musicservice.SpotifyService
import id.derysudrajat.harpify.data.remote.response.toSearchResults
import id.derysudrajat.harpify.data.repositories.tokenrepository.TokenRepository
import id.derysudrajat.harpify.domain.SearchResult

@Suppress("FunctionName")
fun SpotifyTrackSearchPagingSource(
    searchQuery: String,
    countryCode: String,
    tokenRepository: TokenRepository,
    spotifyService: SpotifyService
): SpotifySearchPagingSource<SearchResult.TrackSearchResult> = SpotifySearchPagingSource(
    searchQuery = searchQuery,
    countryCode = countryCode,
    searchQueryType = SearchQueryType.TRACK,
    tokenRepository = tokenRepository,
    spotifyService = spotifyService,
    resultsBlock = { it.toSearchResults().tracks }
)