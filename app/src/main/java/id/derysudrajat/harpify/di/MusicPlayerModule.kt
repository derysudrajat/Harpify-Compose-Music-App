package id.derysudrajat.harpify.di

import id.derysudrajat.harpify.musicplayer.MusicPlayerV2
import id.derysudrajat.harpify.musicplayer.MusifyBackgroundMusicPlayerV2
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicPlayerModule {
    @Binds
    @Singleton
    abstract fun bindMusicPlayerV2(
        musifyBackgroundMusicPlayerV2: MusifyBackgroundMusicPlayerV2
    ): MusicPlayerV2
}