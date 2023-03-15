package com.amb.twitterclone.domain.response

open class SendTweetResponse {
    object SendTweetError : SendTweetResponse()
    object SendTweetSuccess : SendTweetResponse()
}
