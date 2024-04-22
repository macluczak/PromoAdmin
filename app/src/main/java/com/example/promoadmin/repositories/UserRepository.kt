package com.example.promoadmin.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.api.user.UserApi
import com.example.api.user.model.User
import com.example.api.user.model.UserRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext private val context: Context
) {

    var userId: String = ""
    var jwtToken: String = ""
    var user: User? = null

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserIdAndToken(userId: String, jwtToken: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, userId)
            putString(JWT_TOKEN_KEY, jwtToken)
            apply()
        }.also { getUserIdAndToken() }
    }

    fun getUserIdAndToken() {
        userId = sharedPreferences.getString(USER_ID_KEY, null).orEmpty()
        jwtToken = sharedPreferences.getString(JWT_TOKEN_KEY, null).orEmpty()
    }
    private fun clearUserIdAndToken() {
        userId = ""
        jwtToken = ""
    }

    suspend fun getUserData(): User {
        getUserIdAndToken()
        try {
            return userApi.getUser(userId, "Bearer $jwtToken")
        } catch (e: HttpException) {
            throw Exception("Problem with fetching user data: HTTP error ${e.code()}")
        } catch (e: IOException) {
            throw Exception("Problem with fetching user data: Network error")
        } catch (e: Exception) {
            throw Exception("Problem with fetching user data: ${e.message}")
        }
    }

    suspend fun getAllUsers(): List<User> =
         userApi.getAllUsers("Bearer $jwtToken")

    suspend fun changePassword(newPassword: String, body: UserRequest): Response<Void> =
        userApi.changePassword(newPassword, "Bearer $jwtToken", body)


    fun isUserLoggedIn(): Boolean {
        getUserIdAndToken()
        return userId.isNotEmpty() && jwtToken.isNotEmpty()
    }

    fun logoutUser() =
        with(sharedPreferences.edit()) {
            remove(USER_ID_KEY)
            remove(JWT_TOKEN_KEY)
            apply()
        }.also { clearUserIdAndToken() }


    companion object {
        const val USER_ID_KEY = "user_id"
        const val JWT_TOKEN_KEY = "jwt_token"
    }
}