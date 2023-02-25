package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.SendTweetResponse
import com.amb.twitterclone.domain.model.Tweet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SendTweetUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore
) {
    suspend operator fun invoke(
        tweet: Tweet
    ): Flow<SendTweetResponse> {
        return callbackFlow {
            try {
                /*firebaseDB.collection(DATABASE_USERS)
                    .document(authRepository.getCurrentUserId())
                    .get()
                    .addOnSuccessListener {
                        val user = it.toObject(User::class.java)
                        if (user != null) {
                            trySend(user.mapToUserData())
                        } else {
                            trySend(ProfileResponse.Error(ErrorType.NO_USER_FOUND))
                        }
                    }
                    .addOnFailureListener {
                        trySend(ProfileResponse.Error(ErrorType.GENERIC))
                    }



                val currentUser = firebaseAuth.getUser()
                val tweetRef = firebaseDB.collection(DATABASE_TWEETS).document()
                val newTweet = tweet.copy(tweetId = tweetRef.id, userIds =currentUser. )
                tweetRef.set(newTweet)
                */
                trySend(SendTweetResponse.Success)
            } catch (e: Exception) {
                trySend(SendTweetResponse.SendTweetError)
            }
        }
    }
}