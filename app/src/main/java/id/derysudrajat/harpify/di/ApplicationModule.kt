package id.derysudrajat.harpify.di

import id.derysudrajat.harpify.data.encoder.AndroidBase64Encoder
import id.derysudrajat.harpify.data.encoder.Base64Encoder
import id.derysudrajat.harpify.data.repositories.tokenrepository.SpotifyTokenRepository
import id.derysudrajat.harpify.data.repositories.tokenrepository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    abstract fun bindBase64Encoder(
        androidBase64Encoder: AndroidBase64Encoder
    ): Base64Encoder

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        spotifyTokenRepository: SpotifyTokenRepository
    ): TokenRepository
}