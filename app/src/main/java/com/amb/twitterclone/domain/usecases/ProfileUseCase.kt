package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.ProfileResponse
import com.amb.twitterclone.domain.model.ProfileResponse.ErrorType
import com.amb.twitterclone.domain.model.User
import com.amb.twitterclone.util.DATABASE_USERS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore
) {
    operator fun invoke(): Flow<ProfileResponse> {
        return callbackFlow {
            try {
                firebaseDB.collection(DATABASE_USERS)
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
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ProfileResponse.Error(ErrorType.GENERIC))
            }
        }
    }

    private fun User.mapToUserData(): ProfileResponse.UserData {
        return ProfileResponse.UserData(
            userEmail = this.email ?: "",
            userName = this.userName ?: "",
            userImageUrl = this.imageUrl ?: "",
            userFollowedHashTags = this.followedHashTags ?: arrayListOf(),
            userFollowedUsers = this.followedUsers ?: arrayListOf(),
        )
    }
}