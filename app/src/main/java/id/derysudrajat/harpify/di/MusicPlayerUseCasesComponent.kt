package id.derysudrajat.harpify.di

import id.derysudrajat.harpify.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import id.derysudrajat.harpify.usecases.getPlaybackLoadingStatusUseCase.MusifyGetPlaybackLoadingStatusUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicPlayerUseCasesComponent {
    @Binds
    abstract fun bindGetPlaybackLoadingStatusUseCase(
        impl: MusifyGetPlaybackLoadingStatusUseCase
    ): GetPlaybackLoadingStatusUseCase
}