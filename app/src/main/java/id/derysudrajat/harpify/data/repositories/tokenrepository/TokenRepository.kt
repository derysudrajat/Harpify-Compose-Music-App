package id.derysudrajat.harpify.data.repositories.tokenrepository

import id.derysudrajat.harpify.data.remote.token.BearerToken

interface TokenRepository {

    suspend fun getValidBearerToken(): BearerToken
}