package id.derysudrajat.harpify.data.repositories.tokenrepository

import id.derysudrajat.harpify.data.remote.token.BearerToken
import id.derysudrajat.harpify.data.utils.FetchedResource
import id.derysudrajat.harpify.domain.MusifyErrorType
import id.derysudrajat.harpify.domain.getAssociatedMusifyErrorType
import com.fasterxml.jackson.core.JacksonException
import retrofit2.HttpException
import java.io.IOException

suspend fun <R> TokenRepository.runCatchingWithToken(block: suspend (BearerToken) -> R): FetchedResource<R, MusifyErrorType> =
    try {
        FetchedResource.Success(block(getValidBearerToken()))
    } catch (httpException: HttpException) {
        FetchedResource.Failure(httpException.getAssociatedMusifyErrorType())
    } catch (ioException: IOException) {
        FetchedResource.Failure(
            if (ioException is JacksonException) MusifyErrorType.DESERIALIZATION_ERROR
            else MusifyErrorType.NETWORK_CONNECTION_FAILURE
        )
    }