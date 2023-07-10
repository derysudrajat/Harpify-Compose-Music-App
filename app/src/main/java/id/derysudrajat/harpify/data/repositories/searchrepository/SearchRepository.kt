package id.derysudrajat.harpify.data.repositories.searchrepository

import androidx.paging.PagingData
import id.derysudrajat.harpify.data.utils.FetchedResource
import id.derysudrajat.harpify.domain.MusifyErrorType
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.domain.SearchResults
import kotlinx.coroutines.flow.Flow


interface SearchRepository {
    suspend fun fetchSearchResultsForQuery(
        searchQuery: String,
        countryCode: String
    ): FetchedResource<SearchResults, MusifyErrorType>


    fun getPaginatedSearchStreamForTracks(
        searchQuery: String,
        countryCode: String
    ): Flow<PagingData<SearchResult.TrackSearchResult>>

}