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
import kotlin.concurrent.thread

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return userRepository.isUserLoggedIn()}

    fun getUser(onSuccess: (User) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData()
                onSuccess(user)
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun logoutUser() = userRepository.logoutUser()


}