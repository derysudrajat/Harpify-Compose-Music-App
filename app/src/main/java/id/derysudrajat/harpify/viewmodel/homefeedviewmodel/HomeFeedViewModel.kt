package id.derysudrajat.harpify.viewmodel.homefeedviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator.GreetingPhraseGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel @Inject constructor(
    application: Application,
    greetingPhraseGenerator: GreetingPhraseGenerator
) : AndroidViewModel(application) {
    val greetingPhrase = greetingPhraseGenerator.generatePhrase()
}