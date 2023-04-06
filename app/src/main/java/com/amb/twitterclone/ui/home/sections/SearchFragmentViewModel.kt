package com.amb.twitterclone.ui.home.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.response.FetchTweetResponse
import com.amb.twitterclone.domain.usecases.FetchTweetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val fetchTweetsUseCase: FetchTweetsUseCase
) : ViewModel() {

    private var _searchTweetLiveData = MutableLiveData<TweetListViewState>()
    val searchTweetLiveData: LiveData<TweetListViewState> get() = _searchTweetLiveData

    fun onNewTermSearched(term: String) {
        viewModelScope.launch {
            fetchTweetsUseCase(term)
                .collect {
                    _searchTweetLiveData.value = if (it is FetchTweetResponse.Success) {
                        TweetListViewState.ShowList(it.tweetList)
                    } else {
                        TweetListViewState.Error
                    }
                }
        }
    }
}