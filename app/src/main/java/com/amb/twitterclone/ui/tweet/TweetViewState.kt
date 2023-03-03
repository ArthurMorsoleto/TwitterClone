package com.amb.twitterclone.ui.tweet

open class TweetViewState {
    object EmptyContent : TweetViewState()
    object Error : TweetViewState()
    object Sent : TweetViewState()
}
