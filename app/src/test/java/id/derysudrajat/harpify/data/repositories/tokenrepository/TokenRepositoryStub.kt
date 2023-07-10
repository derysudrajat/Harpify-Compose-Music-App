package id.derysudrajat.harpify.data.repositories.tokenrepository

import id.derysudrajat.harpify.data.remote.token.BearerToken
import java.time.LocalDateTime

class TokenRepositoryStub : TokenRepository {
    override suspend fun getValidBearerToken() = BearerToken(
        "",
        LocalDateTime.now(),
        60
    )
}