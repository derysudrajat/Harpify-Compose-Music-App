package id.derysudrajat.harpify.data.repositories.searchrepository

import id.derysudrajat.harpify.data.encoder.TestBase64Encoder
import id.derysudrajat.harpify.data.remote.musicservice.SpotifyService
import id.derysudrajat.harpify.data.remote.token.tokenmanager.TokenManager
import id.derysudrajat.harpify.data.repositories.tokenrepository.SpotifyTokenRepository
import id.derysudrajat.harpify.data.utils.FetchedResource
import id.derysudrajat.harpify.di.PagingConfigModule
import id.derysudrajat.harpify.utils.defaultHarpifyJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class HarpifySearchRepositoryTest {
    private lateinit var harpifySearchRepository: HarpifySearchRepository

    @Before
    fun setUp() {
        val spotifyService = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(defaultHarpifyJacksonConverterFactory)
            .build()
            .create(SpotifyService::class.java)
        val tokenManager = Retrofit.Builder()
            .baseUrl("https://accounts.spotify.com/")
            .addConverterFactory(defaultHarpifyJacksonConverterFactory)
            .build()
            .create(TokenManager::class.java)
        harpifySearchRepository = HarpifySearchRepository(
            tokenRepository = SpotifyTokenRepository(
                tokenManager,
                TestBase64Encoder()
            ),
            spotifyService = spotifyService,
            pagingConfig = PagingConfigModule.provideDefaultPagingConfig()
        )
    }

    @Test
    fun fetchSearchResultsTest_validSearchQuery_isSuccessfullyFetched() {
        val query = "Dull Knives"
        val result = runBlocking {
            harpifySearchRepository.fetchSearchResultsForQuery(query, "IN")
        }
        assert(result is FetchedResource.Success)
    }
}