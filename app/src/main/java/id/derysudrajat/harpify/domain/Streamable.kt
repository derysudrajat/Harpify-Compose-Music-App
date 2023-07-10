package id.derysudrajat.harpify.domain

sealed interface Streamable {
    val streamInfo: StreamInfo
}

data class StreamInfo(
    val streamUrl: String?,
    val imageUrl: String,
    val title: String,
    val subtitle: String,
)
