package com.example.promoadmin.feature.authentication

import com.example.promoadmin.repositories.UserRepository
import android.util.Log
import androidx.lifecycle.*
import com.example.api.authorization.model.Login
import com.example.api.user.model.User
import com.example.promoadmin.repositories.AuthRepository
import com.example.promoadmin.util.RegexPatterns.EMAIL_PATTERN
import com.example.promoadmin.util.ValidationObject
import com.example.promoadmin.util.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> get() = _emailError

    private val _passwordError = MutableLiveData<String?>()
    val passwordError: LiveData<String?> get() = _passwordError

    private val _serverError = MutableSharedFlow<String?>()
    val serverError = _serverError.asSharedFlow()

    private val emailValidation = ValidationObject("")
    private val passwordValidation = ValidationObject("")

    private val emailValidator = Validator(emailValidation)
        .addRule { it.isNotBlank() }
        .addRule { it.matches(EMAIL_PATTERN) }
        .addRule { it.length <= 50 }

    private val passwordValidator = Validator(passwordValidation)
        .addRule { it.isNotBlank() }
        .addRule { it.length >= 6 }
        .addRule { it.length <= 20 }

    fun clearAllErrors() {
        _emailError.value = null
        _passwordError.value = null
    }

    fun tryLogin(email: String, password: String, onStart: () -> Unit, onFinish: () -> Unit) {
        emailValidation.value = email
        passwordValidation.value = password

        clearAllErrors()
        if (emailValidator.validate() && passwordValidator.validate()) {
            loginUserInternal(email, password, onStart, onFinish)
        } else {
            _emailError.value = emailValidation.error
            _passwordError.value = passwordValidation.error
        }
    }

    private fun loginUserInternal(email: String, password: String, onStart: () -> Unit, onFinish: () -> Unit) {
        viewModelScope.launch {
            onStart()
            try {
                val response = authRepository.loginUser(email, password)
                if (response.isSuccessful) {
                    handleLoginSuccess(response.body())
                } else {
                    handleLoginFailure()
                }
            } catch (e: Exception) {
                handleException(e)
            }
            onFinish()
        }
    }

    private suspend fun handleLoginSuccess(loginInfo: Login?) {
        loginInfo?.let {
            userRepository.saveUserIdAndToken(it.user.id, it.value) }
        _user.value = getUser()
    }

    suspend fun getUser() = userRepository.getUserData()

    private suspend fun handleLoginFailure() {
        _serverError.emit("Invalid Credentials")
    }

    private suspend fun handleException(exception: Exception) {
        Log.e("AuthViewModel", "Exception during login", exception)
        _serverError.emit("An error occurred during login")
    }

    fun tryRegister(email: String, password: String) {
        emailValidation.value = email
        passwordValidation.value = password

        if (emailValidator.validate() && passwordValidator.validate()) {
            registerUserInternal(email, password)
        } else {
            _emailError.value = emailValidation.error
            _passwordError.value = passwordValidation.error
        }
    }

    private fun registerUserInternal(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.registerUser(email, password)
                if (response.isSuccessful) {
                } else {
                    handleRegisterFailure()
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private suspend fun handleRegisterFailure() {
        _serverError.emit("Invalid Credentials")
    }
}
