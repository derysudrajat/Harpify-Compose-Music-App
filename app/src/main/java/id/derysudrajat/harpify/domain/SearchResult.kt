package id.derysudrajat.harpify.domain

sealed class SearchResult {

    data class TrackSearchResult(
        val id: String,
        val name: String,
        val imageUrlString: String,
        val artistsString: String,
        val trackUrlString: String?
    ) : SearchResult(), Streamable {
        override val streamInfo = StreamInfo(
            streamUrl = trackUrlString,
            imageUrl = imageUrlString,
            title = name,
            subtitle = artistsString
        )
    }
}