package com.example.promoadmin.ui.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.firebase.FirebaseStorageRepository
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import com.example.api.user.model.User
import com.example.api.user.model.UserRequest
import com.example.promoadmin.repositories.ShopRepository
import com.example.promoadmin.repositories.UserRepository
import com.example.promoadmin.util.ValidatorHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _changePasswordResponse = MutableSharedFlow<Int>()
    val changePasswordResponse: SharedFlow<Int> get() = _changePasswordResponse

    init {
        userRepository.getUserIdAndToken()
    }

    fun fetchStoresForUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shops = shopRepository.getShopsForUser(
                    userRepository.userId,
                    userRepository.jwtToken
                )
                _shops.postValue(shops)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val users = userRepository.getAllUsers()
                _users.postValue(users)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = userRepository.getUserData()
                _user.postValue(user)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun logoutUser() = userRepository.logoutUser()


    fun changePassword(newPassword: String, oldPassword: String) = viewModelScope.launch {
        if (!ValidatorHelper.isPasswordValid(newPassword) || !ValidatorHelper.isPasswordValid(oldPassword)) {
            _changePasswordResponse.emit(400)
            return@launch
        }
        try {
            val user = userRepository.getUserData()
            val request = UserRequest(user.id, user.email, oldPassword, user.role)
            val response = userRepository.changePassword(newPassword, request)

            _changePasswordResponse.emit(response.code())
        } catch (e: Exception) {
            e.printStackTrace()
            _changePasswordResponse.emit(400)
        }
    }

    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? = firebaseStorageRepository.uploadImageToFirebase(selectedImageUri)

    fun createShop(shop: ShopRequest, userId: String) =
        viewModelScope.launch {
            try {
                val shop = shopRepository.createShop(userRepository.jwtToken, shop)
                shopRepository.shopAssignUser(shop.id.toString(), userId,userRepository.jwtToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

}