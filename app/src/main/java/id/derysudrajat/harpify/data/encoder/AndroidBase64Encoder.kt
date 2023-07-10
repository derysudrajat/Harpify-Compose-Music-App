package id.derysudrajat.harpify.data.encoder

import android.util.Base64
import javax.inject.Inject

class AndroidBase64Encoder @Inject constructor() : Base64Encoder {
    override fun encodeToString(
        input: ByteArray
    ): String = Base64.encodeToString(input, Base64.NO_WRAP)
}