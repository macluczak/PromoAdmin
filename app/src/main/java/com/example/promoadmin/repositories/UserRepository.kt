package com.example.promoadmin.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.api.authorization.model.Login
import com.example.api.user.UserApi
import com.example.api.user.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    @ApplicationContext private val context: Context
) {

    var userId: String = ""

    var jwtToken: String = ""



    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserIdAndToken(userId: String, jwtToken: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_KEY, userId)
            putString(JWT_TOKEN_KEY, jwtToken)
            apply()
        }
    }

    fun getUserIdAndToken() {
         userId = sharedPreferences.getString(USER_ID_KEY, null).orEmpty()
         jwtToken = sharedPreferences.getString(JWT_TOKEN_KEY, null).orEmpty()
        Log.d("USER_PREFS", "id: ${userId}, jwt: ${jwtToken}")
    }

     suspend fun getUserData(): User =
         userApi.getUser(userId, "Bearer $jwtToken")

    fun isUserLoggedIn(): Boolean {
        getUserIdAndToken()
        return userId.isNotEmpty() && jwtToken.isNotEmpty()
    }

    companion object {
        const val USER_ID_KEY = "user_id"
        const val JWT_TOKEN_KEY = "jwt_token"
    }

}