package id.derysudrajat.harpify.data.paging

import id.derysudrajat.harpify.data.remote.response.fakeTrackResponseWithAlbumMetadata
import id.derysudrajat.harpify.data.remote.response.SearchResultsResponse


enum class FakeSearchResultsPageNumbers(val pageIndexValue: Int) {
    PAGE_1(0),
    PAGE_2(1),
    PAGE_3(2)
}

private val fakeTracks = List(3) { fakeTrackResponseWithAlbumMetadata.copy(id = it.toString()) }

fun getSearchResultsResponseForFakePageNumber(fakePageNumber: FakeSearchResultsPageNumbers) =
    SearchResultsResponse(
        tracks = SearchResultsResponse.Tracks(listOf(fakeTracks[fakePageNumber.pageIndexValue])),
    )
