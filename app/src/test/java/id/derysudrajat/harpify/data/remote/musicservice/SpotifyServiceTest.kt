package id.derysudrajat.harpify.data.remote.musicservice

import id.derysudrajat.harpify.data.encoder.TestBase64Encoder
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import id.derysudrajat.harpify.data.remote.response.AlbumMetadataResponse
import id.derysudrajat.harpify.data.remote.token.BearerToken
import id.derysudrajat.harpify.data.remote.token.tokenmanager.TokenManager
import id.derysudrajat.harpify.data.repositories.tokenrepository.SpotifyTokenRepository
import id.derysudrajat.harpify.utils.defaultHarpifyJacksonConverterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class SpotifyServiceTest {

    private val tokenRepository = SpotifyTokenRepository(
        Retrofit.Builder().baseUrl(SpotifyBaseUrls.AUTHENTICATION_URL)
            .addConverterFactory(defaultHarpifyJacksonConverterFactory).build()
            .create(TokenManager::class.java), TestBase64Encoder()
    )
    lateinit var musicService: SpotifyService

    private fun <R> runBlockingWithToken(block: suspend (BearerToken) -> R): R = runBlocking {
        block(tokenRepository.getValidBearerToken())
    }


    @Before
    fun setup() {
        musicService = Retrofit.Builder().baseUrl("https://api.spotify.com/")
            .addConverterFactory(defaultHarpifyJacksonConverterFactory).build()
            .create(SpotifyService::class.java)
    }


    @Test
    fun buildSearchQueryTest_listOfSearchQueryEnum_buildsValidSearchQuery() {
        val searchQueryTypes = arrayOf(SearchQueryType.TRACK, SearchQueryType.ALBUM)
        val builtQuery = buildSearchQueryWithTypes(*searchQueryTypes)
        assert(builtQuery == "track,album")
    }

    @Test
    fun searchTrackTest_validSearchQueryWithLimit_returnsSearchResultsDTO() {
        val searchQuery = "Visiting hours by Ed Sheeran"
        val searchResultsDTO = runBlockingWithToken {
            musicService.search(
                searchQuery = searchQuery,
                type = buildSearchQueryWithTypes(SearchQueryType.TRACK),
                market = "IN",
                token = it,
                limit = 1 // limiting the number of results to one
            )
        }
        with(searchResultsDTO) {
            assert(tracks != null)
        }
    }

    @Test
    fun searchAlbumTest_validSearchQueryWithLimit_returnsSearchResultsDTO() {
        val searchQuery = "="
        val searchResultsDTO = runBlockingWithToken {
            musicService.search(
                searchQuery = searchQuery,
                type = buildSearchQueryWithTypes(SearchQueryType.ALBUM),
                market = "IN",
                token = it,
                limit = 1 // limiting the number of results to one
            )
        }
        with(searchResultsDTO) {
            assert(tracks == null)
        }
    }

    @Test
    fun searchTestForAllTypes_validSearchQueryWithLimit_returnsSearchResultDTO() {
        val searchQuery = "Anirudh"
        val searchResultsDTO = runBlockingWithToken {
            musicService.search(
                searchQuery = searchQuery,
                market = "IN",
                type = SpotifyEndPoints.Defaults.defaultSearchQueryTypes,
                token = it,
                limit = 1 // limiting the number of results for each type to one
            )
        }
        with(searchResultsDTO) {
            assert(tracks != null)
        }
    }

    @Test(expected = MissingKotlinParameterException::class)
    fun jacksonDeSerializationTest_nullValueForNonNullableKotlinProperty_throwsException() {

        val jsonString = "{\"id\":null,\"name\":\"s\"}"

        jacksonObjectMapper().readValue(
            jsonString, AlbumMetadataResponse.ArtistInfoResponse::class.java
        )
    }



    @Test
    fun searchShowTest_validShowName_returnsAtleastOneShow() {
        runBlockingWithToken {
            val searchResultsDTO = musicService.search(
                searchQuery = "Waveform: The MKBHD Podcast",
                market = "US",
                token = it,
                type = SearchQueryType.TRACK.value
            )
            assert(searchResultsDTO.tracks != null)
            assert(searchResultsDTO.tracks!!.value.isNotEmpty())
        }
    }

}