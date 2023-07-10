package id.derysudrajat.harpify.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import id.derysudrajat.harpify.R
import id.derysudrajat.harpify.utils.conditional


enum class ListItemCardType { ARTIST, TRACK }


@ExperimentalMaterialApi
@Composable
fun HarpifyCompactListItemCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    thumbnailImageUrlString: String? = null,
    thumbnailShape: Shape? = null,
    titleTextStyle: TextStyle = LocalTextStyle.current,
    subtitleTextStyle: TextStyle = LocalTextStyle.current,
    isLoadingPlaceHolderVisible: Boolean = false,
    onThumbnailLoading: (() -> Unit)? = null,
    onThumbnailImageLoadingFinished: ((Throwable?) -> Unit)? = null,
    errorPainter: Painter? = null,
    placeholderHighlight: PlaceholderHighlight = PlaceholderHighlight.shimmer(),
    contentPadding: PaddingValues = PaddingValues(all = 8.dp),
    isPlaybackPaused: Boolean,
    isCurrentlyPlaying: Boolean = false,
) {

    val loadingAnimationComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            R.raw.lottie_wave_play_anim
        )
    )

    Card(
        modifier = Modifier
            .sizeIn(minHeight = 56.dp, minWidth = 250.dp, maxHeight = 80.dp)
            .then(modifier),
        shape = shape,
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            thumbnailImageUrlString?.let {
                AsyncImageWithPlaceholder(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f, true)
                        .conditional(thumbnailShape != null) { clip(thumbnailShape!!) },
                    model = it,
                    contentScale = ContentScale.Crop,
                    isLoadingPlaceholderVisible = isLoadingPlaceHolderVisible,
                    onImageLoading = { onThumbnailLoading?.invoke() },
                    onImageLoadingFinished = { onThumbnailImageLoadingFinished?.invoke(it) },
                    placeholderHighlight = placeholderHighlight,
                    errorPainter = errorPainter,
                    alpha = LocalContentAlpha.current,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = titleTextStyle
                )
                Text(
                    text = subtitle,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = subtitleTextStyle
                )
            }

            AnimatedVisibility(isCurrentlyPlaying && !isPlaybackPaused) {
                LottieAnimation(
                    modifier = Modifier.size(48.dp),
                    composition = loadingAnimationComposition,
                    iterations = LottieConstants.IterateForever
                )
            }
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun HarpifyCompactListItemCard(
    cardType: ListItemCardType,
    title: String,
    subtitle: String,
    thumbnailImageUrlString: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    titleTextStyle: TextStyle = LocalTextStyle.current,
    subtitleTextStyle: TextStyle = LocalTextStyle.current,
    isLoadingPlaceHolderVisible: Boolean = false,
    onThumbnailLoading: (() -> Unit)? = null,
    onThumbnailImageLoadingFinished: ((Throwable?) -> Unit)? = null,
    errorPainter: Painter? = null,
    placeholderHighlight: PlaceholderHighlight = PlaceholderHighlight.shimmer(),
    contentPadding: PaddingValues = PaddingValues(8.dp),
    isPlaybackPaused: Boolean,
    isCurrentlyPlaying: Boolean = false,
) {
    HarpifyCompactListItemCard(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = shape,
        thumbnailImageUrlString = thumbnailImageUrlString,
        title = title,
        subtitle = subtitle,
        onClick = onClick,
        thumbnailShape = if (cardType == ListItemCardType.ARTIST) CircleShape else null,
        titleTextStyle = titleTextStyle,
        subtitleTextStyle = subtitleTextStyle,
        isLoadingPlaceHolderVisible = isLoadingPlaceHolderVisible,
        onThumbnailLoading = onThumbnailLoading,
        onThumbnailImageLoadingFinished = onThumbnailImageLoadingFinished,
        placeholderHighlight = placeholderHighlight,
        errorPainter = errorPainter,
        contentPadding = contentPadding,
        isCurrentlyPlaying = isCurrentlyPlaying,
        isPlaybackPaused = isPlaybackPaused
    )
}