package id.derysudrajat.harpify.data.remote.response

import com.fasterxml.jackson.annotation.JsonProperty

data class AlbumMetadataResponse(
    val id: String,
    val name: String,
    @JsonProperty("album_type") val albumType: String, // album,single or compilation
    val artists: List<ArtistInfoResponse>,
    val images: List<ImageResponse>,
    @JsonProperty("release_date") val releaseDate: String,
    @JsonProperty("release_date_precision") val releaseDatePrecision: String, // year, month or day
    @JsonProperty("total_tracks") val totalTracks: Int,
    val type: String
) {
    data class ArtistInfoResponse(
        val id: String,
        val name: String
    )
}