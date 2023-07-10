package id.derysudrajat.harpify.viewmodel.searchviewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.exoplayer2.util.Util.getCountryCode
import dagger.hilt.android.lifecycle.HiltViewModel
import id.derysudrajat.harpify.data.repositories.searchrepository.SearchRepository
import id.derysudrajat.harpify.di.IODispatcher
import id.derysudrajat.harpify.domain.SearchResult
import id.derysudrajat.harpify.usecases.getCurrentlyPlayingTrackUseCase.GetCurrentlyPlayingTrackUseCase
import id.derysudrajat.harpify.usecases.getPlaybackLoadingStatusUseCase.GetPlaybackLoadingStatusUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


enum class SearchScreenUiState { LOADING, IDLE }

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
    getCurrentlyPlayingTrackUseCase: GetCurrentlyPlayingTrackUseCase,
    getPlaybackLoadingStatusUseCase: GetPlaybackLoadingStatusUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository
) : AndroidViewModel(application) {

    private var searchJob: Job? = null

    private val _uiState = mutableStateOf(SearchScreenUiState.IDLE)
    val uiState = _uiState as State<SearchScreenUiState>

    private val _currentlySelectedFilter = mutableStateOf(SearchFilter.TRACKS)
    val currentlySelectedFilter = _currentlySelectedFilter as State<SearchFilter>

    private val _trackListForSearchQuery =
        MutableStateFlow<PagingData<SearchResult.TrackSearchResult>>(PagingData.empty())
    val trackListForSearchQuery =
        _trackListForSearchQuery as Flow<PagingData<SearchResult.TrackSearchResult>>


    val currentlyPlayingTrackStream = getCurrentlyPlayingTrackUseCase.currentlyPlayingTrackStream

    init {
        getPlaybackLoadingStatusUseCase
            .loadingStatusStream
            .onEach { isPlaybackLoading ->
                if (isPlaybackLoading && _uiState.value != SearchScreenUiState.LOADING) {
                    _uiState.value = SearchScreenUiState.LOADING
                    return@onEach
                }
                if (!isPlaybackLoading && _uiState.value == SearchScreenUiState.LOADING) {
                    _uiState.value = SearchScreenUiState.IDLE
                    return@onEach
                }
            }
            .launchIn(viewModelScope)
    }

    private fun collectAndAssignSearchResults(searchQuery: String) {
        searchRepository.getPaginatedSearchStreamForTracks(
            searchQuery = searchQuery,
            countryCode = getCountryCode(getApplication<Application>().applicationContext)
        ).cachedIn(viewModelScope)
            .collectInViewModelScopeUpdatingUiState(currentlySelectedFilter.value == SearchFilter.TRACKS) {
                _trackListForSearchQuery.value = it
            }

    }

    private fun setEmptyValuesToAllSearchResults() {
        _trackListForSearchQuery.value = PagingData.empty()
    }

    private fun <T> Flow<T>.collectInViewModelScopeUpdatingUiState(
        updateUiStatePredicate: Boolean,
        collectBlock: (T) -> Unit
    ) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                collect {
                    collectBlock(it)
                    if (_uiState.value == SearchScreenUiState.LOADING && updateUiStatePredicate)
                        _uiState.value = SearchScreenUiState.IDLE
                }
            }
        }
    }

    fun search(searchQuery: String) {
        searchJob?.cancel()
        if (searchQuery.isBlank()) {
            setEmptyValuesToAllSearchResults()
            _uiState.value = SearchScreenUiState.IDLE
            return
        }
        _uiState.value = SearchScreenUiState.LOADING
        searchJob = viewModelScope.launch {
            delay(500)
            collectAndAssignSearchResults(searchQuery)
        }
    }

}