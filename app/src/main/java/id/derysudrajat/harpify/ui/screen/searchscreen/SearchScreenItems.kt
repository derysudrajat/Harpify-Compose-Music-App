package id.derysudrajat.harpify.ui.screen.searchscreen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.ui.component.DefaultHarpifyErrorMessage
import id.derysudrajat.harpify.ui.component.ListItemCardType
import id.derysudrajat.harpify.ui.component.HarpifyCompactTrackCard

private val CardBackgroundColor @Composable get() = Color.Transparent
private val CardShape = RectangleShape

@ExperimentalMaterialApi
fun LazyListScope.searchTrackListItems(
    tracksListForSearchQuery: LazyPagingItems<SearchResult.TrackSearchResult>,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?,
    isPlaybackPaused: Boolean,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult.TrackSearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult.TrackSearchResult, Throwable?) -> Unit,
) {
    itemsIndexedWithEmptyListContent(
        items = tracksListForSearchQuery,
        cardType = ListItemCardType.TRACK,
        key = { index, track -> "$index${track.id}" },
    ) { _, track ->
        track?.let {
            HarpifyCompactTrackCard(
                backgroundColor = CardBackgroundColor,
                shape = CardShape,
                track = it,
                onClick = onItemClick,
                isLoadingPlaceholderVisible = isLoadingPlaceholderVisible(it),
                onImageLoading = onImageLoading,
                onImageLoadingFinished = onImageLoadingFinished,
                isCurrentlyPlaying = it == currentlyPlayingTrack,
                isPlaybackPaused = isPlaybackPaused
            )
        }
    }
}


private fun <T : Any> LazyListScope.itemsIndexedWithEmptyListContent(
    items: LazyPagingItems<T>,
    cardType: ListItemCardType? = null,
    key: ((index: Int, item: T) -> Any)? = null,
    emptyListContent: @Composable LazyItemScope.() -> Unit = {
        val title = remember(cardType) {
            "Couldn't find any tracks matching the search query."
        }
        DefaultHarpifyErrorMessage(
            title = title,
            subtitle = "Try searching again using a different spelling or keyword.",
            modifier = Modifier
                .fillParentMaxSize()
                .padding(horizontal = 16.dp)
                .windowInsetsPadding(WindowInsets.ime),
            onRetryButtonClicked = {}
        )
    },
    itemContent: @Composable LazyItemScope.(index: Int, value: T?) -> Unit
) {
    if (items.loadState.append.endOfPaginationReached && items.itemCount == 0) {
        item { emptyListContent.invoke(this) }
    } else {
        itemsIndexed(items, key, itemContent)
    }
}
