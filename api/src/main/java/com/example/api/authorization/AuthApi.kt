package com.example.api.authorization

import com.example.api.authorization.model.Login
import com.example.api.authorization.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("/login/{email}/{password}")
    suspend fun loginUser(
        @Path("email") email: String,
        @Path("password") password: String
    ): Response<Login>

    @POST("/register")
    suspend fun registerUser(
        @Body request: RegistrationRequest
    ): Response<Void>

}