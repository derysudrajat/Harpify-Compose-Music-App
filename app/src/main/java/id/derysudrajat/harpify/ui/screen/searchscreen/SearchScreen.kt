package id.derysudrajat.harpify.ui.screen.searchscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.ui.component.DefaultHarpifyErrorMessage
import id.derysudrajat.harpify.ui.component.DefaultHarpifyLoadingAnimation

data class PagingItemsForSearchScreen(
    val tracksListForSearchQuery: LazyPagingItems<SearchResult.TrackSearchResult>,
)

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
@OptIn(ExperimentalLayoutApi::class)
fun SearchScreen(
    greeting: String,
    pagingItems: PagingItemsForSearchScreen,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?,
    isPlaybackPaused: Boolean,
    onSearchTextChanged: (searchText: String) -> Unit,
    onErrorRetryButtonClick: (searchQuery: String) -> Unit,
    isLoading: Boolean,
    isSearchErrorMessageVisible: Boolean,
    onSearchQueryItemClicked: (SearchResult) -> Unit,
    onImeDoneButtonClicked: KeyboardActionScope.(searchText: String) -> Unit,
    isFullScreenNowPlayingOverlayScreenVisible: Boolean
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSearchListVisible by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isSearchItemLoadingPlaceholderVisibleMap = remember {
        mutableStateMapOf<SearchResult, Boolean>()
    }

    BackHandler(isSearchListVisible && !isFullScreenNowPlayingOverlayScreenVisible) {
        focusManager.clearFocus()
        if (searchText.isNotEmpty()) {
            searchText = ""

            onSearchTextChanged(searchText)
        }
        isSearchListVisible = false
    }
    val lazyListState = rememberLazyListState()
    val searchBarBackgroundAlpha by remember(isSearchListVisible) {
        mutableStateOf(if (isSearchListVisible) 0.8f else 1f)
    }
    val searchBarAlpha by animateFloatAsState(targetValue = searchBarBackgroundAlpha)
    Column(modifier = Modifier.fillMaxWidth()) {
        SearchBar(
            modifier = Modifier
                .background(MaterialTheme.colors.background.copy(alpha = searchBarAlpha))
                .statusBarsPadding()
                .padding(top = 16.dp),
            title = greeting,
            isSearchListVisible = isSearchListVisible,
            searchText = searchText,
            onCloseTextFieldButtonClicked = {
                searchText = ""
                // notify the caller that the search text is empty
                onSearchTextChanged("")
            },
            onImeDoneButtonClicked = {
                onImeDoneButtonClicked(
                    this, searchText
                )
            },
            onTextFieldFocusChanged = { if (it.isFocused) isSearchListVisible = true },
            onSearchTextChanged = {
                searchText = it
                onSearchTextChanged(it)
            }
        )
        AnimatedContent(targetState = isSearchListVisible,
            transitionSpec = { fadeIn() with fadeOut() }) { targetState ->
            when (targetState) {
                true -> SearchQueryList(
                    pagingItems = pagingItems,
                    onItemClick = { onSearchQueryItemClicked(it) },
                    isLoadingPlaceholderVisible = { item ->
                        isSearchItemLoadingPlaceholderVisibleMap.getOrPut(item) { false }
                    },
                    onImageLoadingFinished = { item, _ ->
                        isSearchItemLoadingPlaceholderVisibleMap[item] = false
                    },
                    onImageLoading = {
                        isSearchItemLoadingPlaceholderVisibleMap[it] = true
                    },
                    isSearchResultsLoadingAnimationVisible = isLoading,
                    currentlyPlayingTrack = currentlyPlayingTrack,
                    isPlaybackPaused = isPlaybackPaused,
                    lazyListState = lazyListState,
                    isSearchErrorMessageVisible = isSearchErrorMessageVisible,
                    onErrorRetryButtonClick = { onErrorRetryButtonClick(searchText) }
                )
                false -> EmptyLoading(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .padding(top = 16.dp)
                )
            }
        }
    }
}

@ExperimentalLayoutApi
@ExperimentalMaterialApi
@Composable
private fun SearchQueryList(
    pagingItems: PagingItemsForSearchScreen,
    onItemClick: (SearchResult) -> Unit,
    isLoadingPlaceholderVisible: (SearchResult) -> Boolean,
    onImageLoading: (SearchResult) -> Unit,
    onImageLoadingFinished: (SearchResult, Throwable?) -> Unit,
    currentlyPlayingTrack: SearchResult.TrackSearchResult?,
    isPlaybackPaused: Boolean,
    lazyListState: LazyListState = rememberLazyListState(),
    isSearchResultsLoadingAnimationVisible: Boolean = false,
    isSearchErrorMessageVisible: Boolean = false,
    onErrorRetryButtonClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        if (isSearchErrorMessageVisible) {
            DefaultHarpifyErrorMessage(
                title = "Oops! Something doesn't look right",
                subtitle = "Please check the internet connection",
                modifier = Modifier
                    .align(Alignment.Center)
                    .imePadding(),
                onRetryButtonClicked = onErrorRetryButtonClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colors.background.copy(alpha = 0.7f))
                    .fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                searchTrackListItems(
                    tracksListForSearchQuery = pagingItems.tracksListForSearchQuery,
                    onItemClick = onItemClick,
                    isLoadingPlaceholderVisible = isLoadingPlaceholderVisible,
                    onImageLoading = onImageLoading,
                    onImageLoadingFinished = onImageLoadingFinished,
                    currentlyPlayingTrack = currentlyPlayingTrack,
                    isPlaybackPaused = isPlaybackPaused,
                )
                item {
                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                }
            }
        }
        val musifyLoadingAnimationModifier = if (WindowInsets.isImeVisible) {
            Modifier
                .align(Alignment.Center)
                .imePadding()
        } else {
            Modifier
                .align(Alignment.Center)
        }
        DefaultHarpifyLoadingAnimation(
            modifier = musifyLoadingAnimationModifier,
            isVisible = isSearchResultsLoadingAnimationVisible
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun SearchBar(
    title: String,
    searchText: String,
    isSearchListVisible: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onCloseTextFieldButtonClicked: () -> Unit,
    onTextFieldFocusChanged: (FocusState) -> Unit,
    onImeDoneButtonClicked: KeyboardActionScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val isClearSearchTextButtonVisible by remember(isSearchListVisible, searchText) {
        mutableStateOf(isSearchListVisible && searchText.isNotEmpty())
    }
    val textFieldTrailingIcon = @Composable {
        AnimatedVisibility(visible = isClearSearchTextButtonVisible,
            enter = fadeIn() + slideInHorizontally { it },
            exit = slideOutHorizontally { it } + fadeOut()) {
            IconButton(onClick = onCloseTextFieldButtonClicked,
                content = { Icon(imageVector = Icons.Filled.Close, contentDescription = null) })
        }
    }
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged(onTextFieldFocusChanged),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = null
                )
            },
            trailingIcon = textFieldTrailingIcon,
            placeholder = {
                Text(
                    text = "Find your mood song now", fontWeight = FontWeight.SemiBold
                )
            },
            singleLine = true,
            value = searchText,
            onValueChange = onSearchTextChanged,
            textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.SemiBold),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                placeholderColor = Color.Black,
                textColor = Color.Black
            ),
            keyboardActions = KeyboardActions(onDone = onImeDoneButtonClicked)
        )
    }
}