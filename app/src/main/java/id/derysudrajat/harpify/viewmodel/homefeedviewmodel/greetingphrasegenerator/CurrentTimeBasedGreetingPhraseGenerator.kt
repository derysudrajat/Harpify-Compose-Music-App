package id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import javax.inject.Inject


class CurrentTimeBasedGreetingPhraseGenerator @Inject constructor() : GreetingPhraseGenerator {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun generatePhrase(): String =
        TimeBasedGreetingPhraseGenerator(LocalTime.now()).generatePhrase()

}