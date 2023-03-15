package com.amb.twitterclone.ui.tweet

import android.net.Uri

open class TweetViewState {
    data class PreviewImage(val uri: Uri) : TweetViewState()
    object EmptyContent : TweetViewState()
    object Sent : TweetViewState()
    object Error : TweetViewState()
}
