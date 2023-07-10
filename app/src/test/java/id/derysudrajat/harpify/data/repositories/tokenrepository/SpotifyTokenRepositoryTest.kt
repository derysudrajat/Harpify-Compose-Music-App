package id.derysudrajat.harpify.data.repositories.tokenrepository

import id.derysudrajat.harpify.data.encoder.TestBase64Encoder
import id.derysudrajat.harpify.data.remote.token.AccessTokenResponse
import id.derysudrajat.harpify.data.remote.token.isExpired
import id.derysudrajat.harpify.data.remote.token.tokenmanager.TokenManager
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SpotifyTokenRepositoryTest {
    private lateinit var tokenRepository: TokenRepository

    @Before
    fun setUp() {
        val tokenManagerMock = object : TokenManager {
            private var numberOfCallsToGetNewAccessToken = 0

            override suspend fun getNewAccessToken(
                secret: String,
                grantType: String
            ): AccessTokenResponse = AccessTokenResponse(
                accessToken = "Fake Token",
                secondsUntilExpiration = if (++numberOfCallsToGetNewAccessToken == 1) 0
                else 3600,
                tokenType = "Fake Token"
            )
        }
        tokenRepository = SpotifyTokenRepository(tokenManagerMock, TestBase64Encoder())
    }

    @Test
    fun getTokenTest_repositoryContainsExpiredTokenWhenRequesting_automaticallyGetsAndReturnsNewToken() =
        runBlocking {
            assert(tokenRepository.getValidBearerToken().isExpired)
            assert(!tokenRepository.getValidBearerToken().isExpired)
        }


}