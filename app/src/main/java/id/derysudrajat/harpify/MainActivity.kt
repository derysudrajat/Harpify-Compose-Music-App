package id.derysudrajat.harpify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.dynamicBackground
import id.derysudrajat.harpify.ui.screen.homescreen.ExpandableMiniPlayerWithSnackbar
import id.derysudrajat.harpify.ui.screen.homescreen.HomeScreen
import id.derysudrajat.harpify.ui.screen.searchscreen.PagingItemsForSearchScreen
import id.derysudrajat.harpify.ui.screen.searchscreen.SearchScreen
import id.derysudrajat.harpify.ui.theme.HarpifyTheme
import id.derysudrajat.harpify.viewmodel.homefeedviewmodel.PlaybackViewModel
import id.derysudrajat.harpify.viewmodel.searchviewmodel.SearchScreenUiState
import id.derysudrajat.harpify.viewmodel.searchviewmodel.SearchViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HarpifyTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                    content = { HarpifyApp() })
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HarpifyApp(){
    val playbackViewModel = hiltViewModel<PlaybackViewModel>()
    val playbackState by playbackViewModel.playbackState
    val snackbarHostState = remember { SnackbarHostState() }
    val playbackEvent: PlaybackViewModel.Event? by playbackViewModel.playbackEventsFlow.collectAsState(
        initial = null
    )
    val miniPlayerStreamable = remember(playbackState) {
        playbackState.currentlyPlayingStreamable ?: playbackState.previouslyPlayingStreamable
    }
    var isNowPlayingScreenVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = playbackEvent) {
        if (playbackEvent !is PlaybackViewModel.Event.PlaybackError) return@LaunchedEffect
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = (playbackEvent as PlaybackViewModel.Event.PlaybackError).errorMessage,
        )
    }
    val isPlaybackPaused = remember(playbackState) {
        playbackState is PlaybackViewModel.PlaybackState.Paused || playbackState is PlaybackViewModel.PlaybackState.PlaybackEnded
    }

    BackHandler(isNowPlayingScreenVisible) {
        isNowPlayingScreenVisible = false
    }

    Box(modifier = Modifier.fillMaxSize()){
        HomeScreen(playbackViewModel::playStreamable, isPlaybackPaused)
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            targetState = miniPlayerStreamable,
        ) { state ->
            if (state == null) {
                SnackbarHost(hostState = snackbarHostState)
            } else {
                ExpandableMiniPlayerWithSnackbar(
                    modifier = Modifier
                        .animateEnterExit(
                            enter = fadeIn() + slideInVertically { it },
                            exit = fadeOut() + slideOutVertically { -it }
                        ),
                    streamable = miniPlayerStreamable!!,
                    onPauseButtonClicked = playbackViewModel::pauseCurrentlyPlayingTrack,
                    onPlayButtonClicked = playbackViewModel::resumeIfPausedOrPlay,
                    isPlaybackPaused = isPlaybackPaused,
                    snackbarHostState = snackbarHostState
                )
            }
        }

    }
}
