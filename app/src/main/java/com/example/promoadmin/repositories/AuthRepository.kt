package com.example.promoadmin.repositories

import com.example.api.authorization.AuthApi
import com.example.api.authorization.model.Login
import com.example.api.authorization.model.RegistrationRequest
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApi: AuthApi) {

    suspend fun loginUser(email: String, password: String): Response<Login> =
        authApi.loginUser(email, password)

    suspend fun registerUser(email: String, password: String): Response<Void> =
        authApi.registerUser(RegistrationRequest(email, password))

}