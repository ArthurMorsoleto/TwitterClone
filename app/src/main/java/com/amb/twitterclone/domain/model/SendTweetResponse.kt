package com.amb.twitterclone.domain.model

open class SendTweetResponse {
    object SendTweetError : SendTweetResponse()
    object SendTweetSuccess : SendTweetResponse()
}
