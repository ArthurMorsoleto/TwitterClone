package com.amb.twitterclone.domain.response

import com.amb.twitterclone.domain.model.Tweet

sealed class FetchTweetResponse {
    object Error : FetchTweetResponse()
    data class Success(val tweetList: ArrayList<Tweet>) : FetchTweetResponse()
}
