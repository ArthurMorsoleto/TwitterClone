package com.amb.twitterclone.util

/**
 * Name of the Collection for the Users DataBase
 */
const val DATABASE_USERS = "Users"
const val DATABASE_IMAGES = "ProfileImages"

/**
 * Fields from [DATABASE_USERS]
 */
const val DATA_USER_EMAIL = "email"
const val DATA_USER_USERNAME = "userName"
const val DATA_USER_IMAGE_URL = "imageUrl"
const val DATA_USER_FOLLOWED_HASHTAGS = "followedHashTags"
const val DATA_USER_FOLLOWED_USERS = "followedUsers"

const val INTENT_TYPE_IMAGE = "image/*"

/**
 * Name of the Collection for the Tweets DataBase
 */
const val DATABASE_TWEETS = "Tweets"

/**
 * Fields from [DATABASE_TWEETS]
 */
const val DATA_TWEET_ID = "tweetId"
const val DATA_TWEET_USERS_ID = "userIds"
const val DATA_TWEET_LIKES = "likes"
const val DATA_TWEET_TEXT = "text"
const val DATA_TWEET_HASHTAGS = "hashTags"

const val DATABASE_TWEET_IMAGES = "TweetImages"
// TODO const val DATA_TWEET_USERNAME = "userName"
// TODO const val DATA_TWEET_IMAGE_URL = "imageUrl"
// TODO const val DATA_TWEET_TIMESTAMP = "timestamp"
