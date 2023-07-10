package id.derysudrajat.harpify.data.remote.response

import id.derysudrajat.harpify.data.utils.MapperImageSize
import id.derysudrajat.harpify.data.utils.getImageResponseForImageSize
import id.derysudrajat.harpify.domain.SearchResult.TrackSearchResult
import com.fasterxml.jackson.annotation.JsonProperty

data class TrackResponseWithAlbumMetadata(
    val id: String,
    val name: String,
    @JsonProperty("preview_url") val previewUrl: String?,
    @JsonProperty("is_playable") val isPlayable: Boolean,
    val explicit: Boolean,
    @JsonProperty("duration_ms") val durationInMillis: Int,
    @JsonProperty("album") val albumMetadata: AlbumMetadataResponse
)

fun TrackResponseWithAlbumMetadata.toTrackSearchResult() =
    TrackSearchResult(
        id = id,
        name = name,
        imageUrlString = albumMetadata.images.getImageResponseForImageSize(MapperImageSize.LARGE).url,
        artistsString = albumMetadata.artists.joinToString(",") { it.name },
        trackUrlString = previewUrl
    )
