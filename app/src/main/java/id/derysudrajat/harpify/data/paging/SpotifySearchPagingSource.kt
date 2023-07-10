package id.derysudrajat.harpify.data.paging

import id.derysudrajat.harpify.data.remote.musicservice.SearchQueryType
import id.derysudrajat.harpify.data.remote.musicservice.SpotifyService
import id.derysudrajat.harpify.data.remote.response.SearchResultsResponse
import id.derysudrajat.harpify.data.repositories.tokenrepository.TokenRepository
import id.derysudrajat.harpify.domain.SearchResult
import retrofit2.HttpException
import java.io.IOException


class SpotifySearchPagingSource<T : SearchResult>(
    searchQuery: String,
    countryCode: String,
    searchQueryType: SearchQueryType,
    tokenRepository: TokenRepository,
    spotifyService: SpotifyService,
    resultsBlock: (SearchResultsResponse) -> List<T>
) : SpotifyPagingSource<T>(
    loadBlock = { limit: Int, offset: Int ->
        try {
            val searchResultsResponse = spotifyService.search(
                searchQuery = searchQuery,
                market = countryCode,
                token = tokenRepository.getValidBearerToken(),
                limit = limit,
                offset = offset,
                type = searchQueryType.value
            )
            SpotifyLoadResult.PageData(resultsBlock(searchResultsResponse))
        } catch (httpException: HttpException) {
            SpotifyLoadResult.Error(httpException)
        } catch (ioException: IOException) {
            SpotifyLoadResult.Error(ioException)
        }
    }
)

sealed class SpotifyLoadResult<Value : Any> {
    data class PageData<Value : Any>(val data: List<Value>) : SpotifyLoadResult<Value>()
    data class Error<Value : Any>(val throwable: Throwable) : SpotifyLoadResult<Value>()
}
