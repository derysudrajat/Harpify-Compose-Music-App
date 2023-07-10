package id.derysudrajat.harpify.musicplayer

import android.content.Context
import android.graphics.Bitmap
import id.derysudrajat.harpify.musicplayer.utils.MediaDescriptionAdapter
import id.derysudrajat.harpify.musicplayer.utils.getCurrentPlaybackProgressFlow
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import id.derysudrajat.harpify.R
import id.derysudrajat.harpify.domain.Streamable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class MusifyBackgroundMusicPlayerV2 @Inject constructor(
    @ApplicationContext context: Context,
    private val exoPlayer: ExoPlayer
) : MusicPlayerV2 {
    private var currentlyPlayingStreamable: Streamable? = null
    private val notificationManagerBuilder by lazy {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setChannelImportance(NotificationUtil.IMPORTANCE_LOW)
            .setChannelNameResourceId(R.string.notification_channel_name)
            .setChannelDescriptionResourceId(R.string.notification_channel_description)
    }

    override val currentPlaybackStateStream: Flow<MusicPlayerV2.PlaybackState> = callbackFlow {
        val listener = createEventsListener { player, events ->
            if (!events.containsAny(
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAYER_ERROR,
                    Player.EVENT_IS_PLAYING_CHANGED,
                    Player.EVENT_IS_LOADING_CHANGED
                )
            ) return@createEventsListener
            val isPlaying =
                events.contains(Player.EVENT_IS_PLAYING_CHANGED) && player.playbackState == Player.STATE_READY && player.playWhenReady
            val isPaused =
                events.contains(Player.EVENT_IS_PLAYING_CHANGED) && player.playbackState == Player.STATE_READY && !player.playWhenReady
            val newPlaybackState = when {
                events.contains(Player.EVENT_PLAYER_ERROR) -> MusicPlayerV2.PlaybackState.Error
                isPlaying -> currentlyPlayingStreamable?.let { buildPlayingState(it, player) }
                isPaused -> currentlyPlayingStreamable?.let(MusicPlayerV2.PlaybackState::Paused)
                player.playbackState == Player.STATE_IDLE -> MusicPlayerV2.PlaybackState.Idle
                player.playbackState == Player.STATE_ENDED -> currentlyPlayingStreamable?.let(
                    MusicPlayerV2.PlaybackState::Ended
                )
                player.isLoading -> MusicPlayerV2.PlaybackState.Loading(previouslyPlayingStreamable = currentlyPlayingStreamable)
                else -> null
            } ?: return@createEventsListener
            trySend(newPlaybackState)
        }
        exoPlayer.addListener(listener)
        awaitClose { exoPlayer.removeListener(listener) }
    }.distinctUntilChanged()
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(500),
            initialValue = MusicPlayerV2.PlaybackState.Idle
        )

    private fun createEventsListener(onEvents: (Player, Player.Events) -> Unit) =
        object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                onEvents(player, events)
            }
        }

    private fun buildPlayingState(
        streamable: Streamable,
        player: Player,
    ) = MusicPlayerV2.PlaybackState.Playing(
        currentlyPlayingStreamable = streamable,
        totalDuration = player.duration,
        currentPlaybackPositionInMillisFlow = player.getCurrentPlaybackProgressFlow()
    )

    override fun playStreamable(
        streamable: Streamable,
        associatedAlbumArt: Bitmap
    ) {
        with(exoPlayer) {
            if (streamable.streamInfo.streamUrl == null) return@with
            if (currentlyPlayingStreamable == streamable) {
                seekTo(0)
                playWhenReady = true
                return@with
            }
            if (isPlaying) exoPlayer.stop()
            currentlyPlayingStreamable = streamable
            setMediaItem(MediaItem.fromUri(streamable.streamInfo.streamUrl!!))
            prepare()
            val mediaDescriptionAdapter = MediaDescriptionAdapter(
                getCurrentContentTitle = { streamable.streamInfo.title },
                getCurrentContentText = { streamable.streamInfo.subtitle },
                getCurrentLargeIcon = { _, _ -> associatedAlbumArt }
            )
            notificationManagerBuilder
                .setMediaDescriptionAdapter(mediaDescriptionAdapter)
                .build().setPlayer(exoPlayer)
            play()
        }
    }

    override fun pauseCurrentlyPlayingTrack() {
        exoPlayer.pause()
    }

    override fun stopPlayingTrack() {
        exoPlayer.stop()
    }

    override fun tryResume(): Boolean {
        val hasPlaybackEnded = exoPlayer.currentPosition > exoPlayer.duration
        if (hasPlaybackEnded) return false
        if (exoPlayer.isPlaying) return false
        return currentlyPlayingStreamable?.let {
            exoPlayer.playWhenReady = true
            true
        } ?: false
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID =
            "com.example.musify.musicplayer.MusicPlayerV2Service.NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_ID = 1
    }
}