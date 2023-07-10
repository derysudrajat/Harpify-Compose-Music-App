package id.derysudrajat.harpify.di

import id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase.DownloadDrawableFromUrlUseCase
import id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase.HarpifyDownloadDrawableFromUrlUseCase
import id.derysudrajat.harpify.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import id.derysudrajat.harpify.usecases.getCurrentlyPlayingTrackUseCase.MusifyGetCurrentlyPlayingTrackUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TrackUseCasesComponent {
    @Binds
    abstract fun bindDownloadDrawableFromUrlUseCase(
        impl: HarpifyDownloadDrawableFromUrlUseCase
    ): DownloadDrawableFromUrlUseCase

    @Binds
    abstract fun bindGetCurrentlyPlayingTrackUseCase(
        impl: MusifyGetCurrentlyPlayingTrackUseCase
    ): GetCurrentlyPlayingTrackUseCase
}