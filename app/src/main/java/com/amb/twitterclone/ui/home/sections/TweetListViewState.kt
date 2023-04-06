package com.amb.twitterclone.ui.home.sections

import com.amb.twitterclone.domain.model.Tweet

sealed class TweetListViewState {
    object Error : TweetListViewState()
    data class ShowList(val tweets: ArrayList<Tweet>) : TweetListViewState()
}