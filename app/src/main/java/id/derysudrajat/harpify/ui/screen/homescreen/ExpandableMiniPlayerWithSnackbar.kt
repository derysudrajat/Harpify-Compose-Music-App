package id.derysudrajat.harpify.ui.screen.homescreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.derysudrajat.harpify.domain.Streamable
import id.derysudrajat.harpify.ui.component.HarpifyMiniPlayer

@ExperimentalAnimationApi
@Composable
fun ExpandableMiniPlayerWithSnackbar(
    streamable: Streamable,
    onPauseButtonClicked: () -> Unit,
    onPlayButtonClicked: (Streamable) -> Unit,
    isPlaybackPaused: Boolean,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var isNowPlayingScreenVisible by remember { mutableStateOf(false) }
    AnimatedContent(
        modifier = modifier,
        targetState = isNowPlayingScreenVisible,
        transitionSpec = {
            slideInVertically(animationSpec = tween()) with shrinkOut(animationSpec = tween())
        }
    ) { isFullScreenVisible ->
        Column {
            SnackbarHost(
                modifier = Modifier.padding(8.dp),
                hostState = snackbarHostState
            )
            HarpifyMiniPlayer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { isNowPlayingScreenVisible = true },
                isPlaybackPaused = isPlaybackPaused,
                streamable = streamable,
                onPlayButtonClicked = { onPlayButtonClicked(streamable) },
                onPauseButtonClicked = onPauseButtonClicked
            )
        }
    }
}

