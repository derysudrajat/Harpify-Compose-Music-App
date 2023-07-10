package id.derysudrajat.harpify.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator.CurrentTimeBasedGreetingPhraseGenerator
import id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator.GreetingPhraseGenerator

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhraseGeneratorModule {
    @Binds
    abstract fun bindCurrentTimeBasedGreetingPhraseGenerator(impl: CurrentTimeBasedGreetingPhraseGenerator): GreetingPhraseGenerator
}