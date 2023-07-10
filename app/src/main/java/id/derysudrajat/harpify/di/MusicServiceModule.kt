package id.derysudrajat.harpify.di

import id.derysudrajat.harpify.data.remote.musicservice.SpotifyBaseUrls
import id.derysudrajat.harpify.data.remote.musicservice.SpotifyService
import id.derysudrajat.harpify.data.remote.token.tokenmanager.TokenManager
import id.derysudrajat.harpify.utils.defaultHarpifyJacksonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicServiceModule {
    @Provides
    @Singleton
    fun provideSpotifyService(): SpotifyService = Retrofit.Builder()
        .baseUrl(SpotifyBaseUrls.API_URL)
        .addConverterFactory(defaultHarpifyJacksonConverterFactory)
        .build()
        .create(SpotifyService::class.java)

    @Provides
    @Singleton
    fun provideTokenManager(): TokenManager = Retrofit.Builder()
        .baseUrl(SpotifyBaseUrls.AUTHENTICATION_URL)
        .addConverterFactory(defaultHarpifyJacksonConverterFactory)
        .build()
        .create(TokenManager::class.java)
}