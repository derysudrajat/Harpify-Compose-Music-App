package id.derysudrajat.harpify.viewmodel.homefeedviewmodel.greetingphrasegenerator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

val LocalTime.isMorning @RequiresApi(Build.VERSION_CODES.O)
get() = this in LocalTime.of(0, 0)..LocalTime.of(11, 59)

val LocalTime.isNoon @RequiresApi(Build.VERSION_CODES.O)
get() = this in LocalTime.of(12, 0)..LocalTime.of(17, 59)
