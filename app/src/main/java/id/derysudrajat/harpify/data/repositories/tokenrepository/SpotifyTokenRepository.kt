package id.derysudrajat.harpify.data.repositories.tokenrepository

import android.os.Build
import androidx.annotation.RequiresApi
import id.derysudrajat.harpify.data.encoder.Base64Encoder
import id.derysudrajat.harpify.data.remote.token.BearerToken
import id.derysudrajat.harpify.data.remote.token.getSpotifyClientSecret
import id.derysudrajat.harpify.data.remote.token.isExpired
import id.derysudrajat.harpify.data.remote.token.toBearerToken
import id.derysudrajat.harpify.data.remote.token.tokenmanager.TokenManager
import javax.inject.Inject

class SpotifyTokenRepository @Inject constructor(
    private val tokenManager: TokenManager,
    private val base64Encoder: Base64Encoder
) : TokenRepository {
    private var token: BearerToken? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getValidBearerToken(): BearerToken {
        if (token == null || token?.isExpired == true) getAndAssignToken()
        return token!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getAndAssignToken() {
        val clientSecret = getSpotifyClientSecret(base64Encoder)
        token = tokenManager
            .getNewAccessToken(clientSecret)
            .toBearerToken()
    }
}