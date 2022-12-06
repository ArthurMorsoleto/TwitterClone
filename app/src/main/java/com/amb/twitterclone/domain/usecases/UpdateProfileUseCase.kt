package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.UpdateResponse
import com.amb.twitterclone.util.DATABASE_USERS
import com.amb.twitterclone.util.DATA_USER_USERNAME
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore
) {
    operator fun invoke(userName: String): Flow<UpdateResponse> {
        val map = HashMap<String, Any>()
        map[DATA_USER_USERNAME] = userName
        return callbackFlow {
            firebaseDB.collection(DATABASE_USERS)
                .document(authRepository.getCurrentUserId())
                .update(map)
                .addOnSuccessListener {
                    trySend(UpdateResponse.Success)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    trySend(UpdateResponse.Error)
                }
        }
    }
}