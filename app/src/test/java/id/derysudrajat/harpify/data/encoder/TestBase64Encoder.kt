package id.derysudrajat.harpify.data.encoder

import id.derysudrajat.harpify.BuildConfig
import java.util.Base64

class TestBase64Encoder : Base64Encoder {
    override fun encodeToString(input: ByteArray): String = Base64
        .getEncoder()
        .encodeToString("${BuildConfig.SPOTIFY_CLIENT_ID}:${BuildConfig.SPOTIFY_CLIENT_SECRET}".toByteArray())
}

