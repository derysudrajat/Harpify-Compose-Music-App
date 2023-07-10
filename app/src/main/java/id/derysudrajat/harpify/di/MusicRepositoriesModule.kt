package id.derysudrajat.harpify.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import id.derysudrajat.harpify.data.repositories.searchrepository.HarpifySearchRepository
import id.derysudrajat.harpify.data.repositories.searchrepository.SearchRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class MusicRepositoriesModule {

    @Binds
    abstract fun bindSearchRepository(impl: HarpifySearchRepository): SearchRepository
}