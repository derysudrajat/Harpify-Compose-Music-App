package id.derysudrajat.harpify.data.remote.musicservice

import id.derysudrajat.harpify.data.remote.response.SearchResultsResponse
import id.derysudrajat.harpify.data.remote.token.BearerToken
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query



interface SpotifyService {


    @GET(SpotifyEndPoints.SEARCH_ENDPOINT)
    suspend fun search(
        @Query("q") searchQuery: String,
        @Query("market") market: String,
        @Header("Authorization") token: BearerToken,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("type") type: String = SpotifyEndPoints.Defaults.defaultSearchQueryTypes,
    ): SearchResultsResponse


}