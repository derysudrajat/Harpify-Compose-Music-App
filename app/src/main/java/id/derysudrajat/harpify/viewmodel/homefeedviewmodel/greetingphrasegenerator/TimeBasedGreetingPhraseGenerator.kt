package id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class TimeBasedGreetingPhraseGenerator constructor(
    private val time: LocalTime = LocalTime.now()
) : GreetingPhraseGenerator {
    override fun generatePhrase(): String = when {
        time.isMorning -> "Good morning"
        time.isNoon -> "Good afternoon"
        else -> "Good evening"
    }
}
