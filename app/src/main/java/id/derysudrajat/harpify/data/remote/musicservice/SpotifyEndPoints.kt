package id.derysudrajat.harpify.data.remote.musicservice

/**
 * An object that contains the different end points
 * used by [SpotifyService]. It also contains certain defaults for the
 * api calls made by [SpotifyService].
 */
object SpotifyEndPoints {
    const val SEARCH_ENDPOINT = "v1/search"
    const val API_TOKEN_ENDPOINT = "api/token"

    object Defaults {
        val defaultSearchQueryTypes = buildSearchQueryWithTypes(*SearchQueryType.values())
    }

}