package com.amb.twitterclone.ui.tweet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.model.SendTweetResponse
import com.amb.twitterclone.domain.usecases.SendTweetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for Send Tweet screen.
 */
@HiltViewModel
class TweetViewModel @Inject constructor(
    private val sendTweetUseCase: SendTweetUseCase
) : ViewModel() {

    private val _tweetViewState = MutableLiveData<TweetViewState>()
    val tweetViewState: LiveData<TweetViewState> get() = _tweetViewState

    /**
     * Verifies current tweet content and call [SendTweetUseCase]
     *
     * @param textContent [String] text typed by the user.
     */
    fun onSendTweetClick(textContent: String) {
        viewModelScope.launch {
            if (textContent.isEmpty()) {
                _tweetViewState.value = TweetViewState.EmptyContent
            } else {
                sendTweetUseCase(tweetContent = textContent).collect { response ->
                    when (response) {
                        is SendTweetResponse.SendTweetError -> {
                            _tweetViewState.value = TweetViewState.Error
                        }
                        is SendTweetResponse.SendTweetSuccess -> {
                            _tweetViewState.value = TweetViewState.Sent
                        }
                    }
                }
            }
        }
    }
}