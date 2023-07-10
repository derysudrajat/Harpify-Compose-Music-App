package id.derysudrajat.harpify.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import id.derysudrajat.harpify.usecases.getCurrentlyPlayingStreamableUseCase.GetCurrentlyPlayingStreamableUseCase
import id.derysudrajat.harpify.usecases.getCurrentlyPlayingStreamableUseCase.HarpifyGetCurrentlyPlayingStreamableUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class PodcastUseCasesComponent {
    @Binds
    abstract fun bindGetCurrentlyPlayingStreamableUseCase(
        impl: HarpifyGetCurrentlyPlayingStreamableUseCase
    ): GetCurrentlyPlayingStreamableUseCase

}