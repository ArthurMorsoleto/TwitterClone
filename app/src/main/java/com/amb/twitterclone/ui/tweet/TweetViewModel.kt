package com.amb.twitterclone.ui.tweet

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amb.twitterclone.domain.response.SendTweetResponse
import com.amb.twitterclone.domain.usecases.SendTweetUseCase
import com.amb.twitterclone.domain.usecases.StoreImageUseCase
import com.amb.twitterclone.util.STORE_IMAGE_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for Send Tweet screen.
 */
@HiltViewModel
class TweetViewModel @Inject constructor(
    private val sendTweetUseCase: SendTweetUseCase,
    private val storeImageUseCase: StoreImageUseCase
) : ViewModel() {

    private val _tweetViewState = MutableLiveData<TweetViewState>()
    val tweetViewState: LiveData<TweetViewState> get() = _tweetViewState

    private val _tweetViewActions = MutableLiveData<TweetViewActions>()
    val tweetViewActions: LiveData<TweetViewActions> get() = _tweetViewActions

    private var currentTweetImage: Uri? = null

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
                val tweetImage = currentTweetImage ?: "".toUri()
                if (tweetImage.toString().isEmpty()) {
                    storeImageUseCase(tweetImage).collect { image ->
                        if (image != STORE_IMAGE_ERROR) {
                            sendTweet(tweetText = textContent, tweetImage = image)
                        }
                    }
                } else {
                    sendTweet(tweetText = textContent)
                }
            }
        }
    }

    /**
     * Trigger Open Camera Action.
     */
    fun onTakePhotoClick() {
        _tweetViewActions.value = TweetViewActions.OpenCamera
    }

    /**
     * Trigger Choose Image Action.
     */
    fun onChoosePhotoClick() {
        _tweetViewActions.value = TweetViewActions.OpenGallery
    }

    /**
     * Trigger Preview Image ViewState.
     *
     * @param uri[Uri] from selected image.
     */
    fun onImageAdded(uri: Uri) {
        currentTweetImage = uri
        _tweetViewState.value = TweetViewState.PreviewImage(uri)
    }

    private suspend fun sendTweet(tweetText: String, tweetImage: String = "") {
        sendTweetUseCase(
            SendTweetUseCase.SendTweetParams(
                tweetText = tweetText,
                tweetImage = tweetImage
            )
        ).collect { response ->
            _tweetViewState.value = when (response) {
                is SendTweetResponse.SendTweetSuccess -> TweetViewState.Sent
                else -> TweetViewState.Error
            }
        }
    }
}