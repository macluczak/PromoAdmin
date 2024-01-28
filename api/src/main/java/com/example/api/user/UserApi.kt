package com.example.api.user

import com.example.api.user.model.User
import com.example.api.user.model.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @POST("/users/changePassword/{newPassword}")
    suspend fun changePassword(
        @Path("newPassword") newPassword: String,
        @Header("Authorization") token: String,
        @Body request: UserRequest,
    ): Response<Void>
}