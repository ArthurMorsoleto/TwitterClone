package com.amb.twitterclone.domain.repository

import com.google.firebase.auth.AuthResult

interface AuthRepository {

    suspend fun loginFireBase(email: String, password: String): AuthResult
}