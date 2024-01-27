package com.example.promoadmin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.user.model.User
import com.example.promoadmin.repositories.AuthRepository
import com.example.promoadmin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        Log.d("USER_PREFS",  userRepository.isUserLoggedIn().toString())
        return userRepository.isUserLoggedIn()}

    suspend fun getUser() = userRepository.getUserData()

    fun logoutUser() = userRepository.logoutUser()


}