package id.derysudrajat.harpify.ui.screen.homescreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.domain.Streamable
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.dynamicBackground
import id.derysudrajat.harpify.ui.screen.searchscreen.PagingItemsForSearchScreen
import id.derysudrajat.harpify.ui.screen.searchscreen.SearchScreen
import id.derysudrajat.harpify.viewmodel.homefeedviewmodel.HomeFeedViewModel
import id.derysudrajat.harpify.viewmodel.searchviewmodel.SearchScreenUiState
import id.derysudrajat.harpify.viewmodel.searchviewmodel.SearchViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    playStreamable: (Streamable) -> Unit,
    isPlaybackPaused: Boolean
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val homeViewModel = hiltViewModel<HomeFeedViewModel>()

    val tracks = viewModel.trackListForSearchQuery.collectAsLazyPagingItems()
    val pagingItems = remember {
        PagingItemsForSearchScreen(tracks)
    }
    val uiState by viewModel.uiState
    val isLoadingError by remember {
        derivedStateOf {
            tracks.loadState.refresh is LoadState.Error || tracks.loadState.append is LoadState.Error || tracks.loadState.prepend is LoadState.Error
        }
    }
    val controller = LocalSoftwareKeyboardController.current
    val dynamicBackgroundResource by remember {
        derivedStateOf {
            val imageUrl = tracks.itemSnapshotList.firstOrNull()?.imageUrlString
            if (imageUrl == null) DynamicBackgroundResource.Empty
            else DynamicBackgroundResource.FromImageUrl(imageUrl)
        }
    }

    val currentlyPlayingTrack by viewModel.currentlyPlayingTrackStream.collectAsState(initial = null)

    Box(modifier = Modifier.dynamicBackground(dynamicBackgroundResource)) {
        SearchScreen(
            greeting = homeViewModel.greetingPhrase,
            onSearchTextChanged = viewModel::search,
            isLoading = uiState == SearchScreenUiState.LOADING,
            pagingItems = pagingItems,
            onSearchQueryItemClicked = {
                if (it is SearchResult.TrackSearchResult) playStreamable.invoke(it)
            },
            isSearchErrorMessageVisible = isLoadingError,
            onImeDoneButtonClicked = {
                if (isLoadingError) viewModel.search(it)
                controller?.hide()
            },
            currentlyPlayingTrack = currentlyPlayingTrack,
            isPlaybackPaused = isPlaybackPaused,
            isFullScreenNowPlayingOverlayScreenVisible = false ,
            onErrorRetryButtonClick = viewModel::search
        )


    }
}