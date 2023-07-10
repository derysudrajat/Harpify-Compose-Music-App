package id.derysudrajat.harpify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
annotation class MainDispatcher

@Qualifier
annotation class IODispatcher

@Qualifier
annotation class DefaultDispatcher


@Module
@InstallIn(ActivityRetainedComponent::class)
object CoroutineDispatchersModule {

    @Provides
    @IODispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}