package id.derysudrajat.harpify.data.encoder

fun interface Base64Encoder {
    fun encodeToString(input: ByteArray): String
}