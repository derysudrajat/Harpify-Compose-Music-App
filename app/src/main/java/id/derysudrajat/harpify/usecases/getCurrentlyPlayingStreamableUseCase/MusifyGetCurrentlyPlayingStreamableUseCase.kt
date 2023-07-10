package id.derysudrajat.harpify.usecases.getCurrentlyPlayingStreamableUseCase

import id.derysudrajat.harpify.domain.Streamable
import id.derysudrajat.harpify.musicplayer.MusicPlayerV2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HarpifyGetCurrentlyPlayingStreamableUseCase @Inject constructor(
    musicPlayer: MusicPlayerV2
) : GetCurrentlyPlayingStreamableUseCase {
    override val currentlyPlayingStreamableStream: Flow<Streamable> = musicPlayer
        .currentPlaybackStateStream
        .filterIsInstance<MusicPlayerV2.PlaybackState.Playing>()
        .map { it.currentlyPlayingStreamable }
}