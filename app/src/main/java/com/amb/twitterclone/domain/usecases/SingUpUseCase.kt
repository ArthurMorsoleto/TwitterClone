package com.amb.twitterclone.domain.usecases

import com.amb.twitterclone.data.AuthRepository
import com.amb.twitterclone.domain.model.SingUpResponse
import com.amb.twitterclone.domain.model.User
import com.amb.twitterclone.util.DATABASE_USERS
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SingUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseDB: FirebaseFirestore
) {
    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String
    ): Flow<SingUpResponse> {
        return flow {
            try {
                val resultId = authRepository.createUserFirebase(email, password).user?.uid
                if (resultId != null) {
                    val user = User(email, userName, "", arrayListOf(), arrayListOf())
                    firebaseDB.collection(DATABASE_USERS)
                        .document(resultId)
                        .set(user)
                    emit(SingUpResponse.Success)
                } else {
                    emit(SingUpResponse.Error)
                }
            } catch (e: Exception) {
                val error = if (e is FirebaseAuthUserCollisionException) {
                    SingUpResponse.UserAlreadyRegistered
                } else {
                    SingUpResponse.Error
                }
                emit(error)
            }
        }
    }
}