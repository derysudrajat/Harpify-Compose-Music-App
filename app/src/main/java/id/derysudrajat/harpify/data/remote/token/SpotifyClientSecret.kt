package id.derysudrajat.harpify.data.remote.token

import id.derysudrajat.harpify.BuildConfig
import id.derysudrajat.harpify.data.encoder.Base64Encoder

fun getSpotifyClientSecret(base64Encoder: Base64Encoder): String {
    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
    val encodedString = base64Encoder.encodeToString("$clientId:$clientSecret".toByteArray())
    return "Basic $encodedString"
}
