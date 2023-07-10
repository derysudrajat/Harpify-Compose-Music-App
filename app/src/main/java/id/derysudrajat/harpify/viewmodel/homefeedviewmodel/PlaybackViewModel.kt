package id.derysudrajat.harpify.viewmodel.homefeedviewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.harpify.domain.Streamable
import id.derysudrajat.harpify.musicplayer.MusicPlayerV2
import id.derysudrajat.harpify.usecases.downloadDrawableFromUrlUseCase.DownloadDrawableFromUrlUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    application: Application,
    private val musicPlayer: MusicPlayerV2,
    private val downloadDrawableFromUrlUseCase: DownloadDrawableFromUrlUseCase
) : AndroidViewModel(application) {

    private val _playbackState = mutableStateOf<PlaybackState>(PlaybackState.Idle)
    val playbackState = _playbackState as State<PlaybackState>

    private val _eventChannel = Channel<Event?>()
    val playbackEventsFlow = _eventChannel.receiveAsFlow()


    private val playbackErrorMessage = "An error occurred. Please check internet connection."

    init {
        musicPlayer.currentPlaybackStateStream.onEach {
            _playbackState.value = when (it) {
                is MusicPlayerV2.PlaybackState.Loading -> PlaybackState.Loading(it.previouslyPlayingStreamable)
                is MusicPlayerV2.PlaybackState.Idle -> PlaybackState.Idle
                is MusicPlayerV2.PlaybackState.Playing -> {
                    PlaybackState.Playing(it.currentlyPlayingStreamable)
                }
                is MusicPlayerV2.PlaybackState.Paused -> PlaybackState.Paused(it.currentlyPlayingStreamable)
                is MusicPlayerV2.PlaybackState.Error -> {
                    viewModelScope.launch {
                        _eventChannel.send(Event.PlaybackError(playbackErrorMessage))
                    }
                    PlaybackState.Error(playbackErrorMessage)
                }
                is MusicPlayerV2.PlaybackState.Ended -> PlaybackState.PlaybackEnded(it.streamable)
            }
        }.launchIn(viewModelScope)
    }

    fun resumeIfPausedOrPlay(streamable: Streamable){
        if(musicPlayer.tryResume()) return
        playStreamable(streamable)
    }

    fun playStreamable(streamable: Streamable) {
        viewModelScope.launch {
            if (streamable.streamInfo.streamUrl == null) {
                _eventChannel.send(Event.PlaybackError("This track is currently unavailable for playback."))
                return@launch
            }

            val downloadAlbumArtResult = downloadDrawableFromUrlUseCase.invoke(
                urlString = streamable.streamInfo.imageUrl,
                context = getApplication()
            )
            if (downloadAlbumArtResult.isSuccess) {
                val bitmap = downloadAlbumArtResult.getOrNull()!!.toBitmap()
                musicPlayer.playStreamable(
                    streamable = streamable,
                    associatedAlbumArt = bitmap
                )
            } else {
                _eventChannel.send(Event.PlaybackError(playbackErrorMessage))
                _playbackState.value = PlaybackState.Error(playbackErrorMessage)
            }
        }
    }

    fun pauseCurrentlyPlayingTrack() {
        musicPlayer.pauseCurrentlyPlayingTrack()
    }


    sealed class PlaybackState(
        val currentlyPlayingStreamable: Streamable? = null,
        val previouslyPlayingStreamable: Streamable? = null
    ) {
        object Idle : PlaybackState()
        object Stopped : PlaybackState()
        data class Error(val errorMessage: String) : PlaybackState()
        data class Paused(val streamable: Streamable) : PlaybackState(streamable)
        data class Playing(val streamable: Streamable) : PlaybackState(streamable)
        data class PlaybackEnded(val streamable: Streamable) : PlaybackState(streamable)
        data class Loading(
            val previousStreamable: Streamable?
        ) : PlaybackState(previouslyPlayingStreamable = previousStreamable)
    }

    sealed class Event {
        class PlaybackError(val errorMessage: String) : Event()
    }
}
