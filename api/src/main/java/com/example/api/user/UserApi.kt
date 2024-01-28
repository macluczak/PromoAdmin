package com.example.api.user

import com.example.api.user.model.User
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {

    @GET("/users/{id}")
    suspend fun getUser(
        @Path("id") userId: String,
        @Header("Authorization") token: String,
    ): User

    @GET("/users/all")
    suspend fun getAllUsers(
        @Header("Authorization") token: String,
    ): List<User>
}