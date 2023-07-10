package id.derysudrajat.harpify.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.derysudrajat.harpify.R
import id.derysudrajat.harpify.domain.Streamable
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundResource
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.DynamicBackgroundStyle
import id.derysudrajat.harpify.ui.dynamicTheme.dynamicbackgroundmodifier.dynamicBackground


object HarpifyMiniPlayerConstants {
    val miniPlayerHeight = 60.dp
}


@Composable
fun HarpifyMiniPlayer(
    streamable: Streamable,
    isPlaybackPaused: Boolean,
    modifier: Modifier = Modifier,
    onPlayButtonClicked: () -> Unit,
    onPauseButtonClicked: () -> Unit
) {
    var isThumbnailImageLoading by remember { mutableStateOf(false) }
    val dynamicBackgroundResource = remember {
        DynamicBackgroundResource.FromImageUrl(streamable.streamInfo.imageUrl)
    }
    val dynamicBackgroundStyle = remember { DynamicBackgroundStyle.Filled() }
    Row(
        modifier = modifier
            .height(HarpifyMiniPlayerConstants.miniPlayerHeight) // the height of this composable is fixed
            .clip(RoundedCornerShape(8.dp))
            .dynamicBackground(dynamicBackgroundResource, dynamicBackgroundStyle),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImageWithPlaceholder(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .aspectRatio(1f),
            model = streamable.streamInfo.imageUrl,
            contentDescription = null,
            onImageLoadingFinished = { isThumbnailImageLoading = false },
            isLoadingPlaceholderVisible = isThumbnailImageLoading,
            onImageLoading = { isThumbnailImageLoading = true },
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = streamable.streamInfo.title,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = streamable.streamInfo.subtitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.caption.copy(
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        IconButton(onClick = {
            if (isPlaybackPaused) onPlayButtonClicked() else onPauseButtonClicked()
        }) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .aspectRatio(1f),
                painter = if (isPlaybackPaused) painterResource(R.drawable.ic_play_arrow_24)
                else painterResource(R.drawable.ic_pause_24),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

