package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.domain.model.Tweet
import com.amb.twitterclone.domain.response.FetchTweetResponse
import com.amb.twitterclone.util.DATABASE_TWEETS
import com.amb.twitterclone.util.DATA_TWEET_HASHTAGS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FetchTweetsUseCase @Inject constructor(
    private val firebaseStorage: FirebaseFirestore
) {
    operator fun invoke(term: String): Flow<FetchTweetResponse> {
        return callbackFlow {
            try {
                firebaseStorage.collection(DATABASE_TWEETS)
                    .whereArrayContains(DATA_TWEET_HASHTAGS, term)
                    .get()
                    .addOnSuccessListener { response ->
                        val tweetList = arrayListOf<Tweet>()
                        for (document in response.documents) {
                            val tweet = document.toObject(Tweet::class.java)
                            tweet?.let { tweetList.add(it) }
                        }
                        trySend(FetchTweetResponse.Success(tweetList))
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        trySend(FetchTweetResponse.Error)
                    }
            } catch (e: Exception) {
                trySend(FetchTweetResponse.Error)
            }
            awaitClose { close() }
        }
    }
}