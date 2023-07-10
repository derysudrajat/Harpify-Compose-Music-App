package id.derysudrajat.harpify.data.repositories.searchrepository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.derysudrajat.harpify.data.paging.SpotifyTrackSearchPagingSource
import id.derysudrajat.harpify.data.remote.musicservice.SpotifyService
import id.derysudrajat.harpify.data.remote.response.toSearchResults
import id.derysudrajat.harpify.data.repositories.tokenrepository.TokenRepository
import id.derysudrajat.harpify.data.repositories.tokenrepository.runCatchingWithToken
import id.derysudrajat.harpify.data.utils.FetchedResource
import id.derysudrajat.harpify.domain.MusifyErrorType
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.domain.SearchResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HarpifySearchRepository @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val spotifyService: SpotifyService,
    private val pagingConfig: PagingConfig
) : SearchRepository {
    override suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        countryCode: String
    ): FetchedResource<SearchResults, MusifyErrorType> = tokenRepository.runCatchingWithToken {
        spotifyService.search(searchQuery, countryCode, it).toSearchResults()
    }

    override fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.TrackSearchResult>> = Pager(pagingConfig) {
        SpotifyTrackSearchPagingSource(
            searchQuery = searchQuery,
            countryCode = countryCode,
            tokenRepository = tokenRepository,
            spotifyService = spotifyService
        )
    }.flow
}
