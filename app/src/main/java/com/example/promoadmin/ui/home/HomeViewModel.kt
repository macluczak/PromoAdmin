package com.example.promoadmin.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.firebase.FirebaseStorageRepository
import com.example.api.shop.ShopApi
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import com.example.api.user.model.User
import com.example.promoadmin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopApi: ShopApi,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _users = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> get() = _users

    init {
        userRepository.getUserIdAndToken()
    }

    fun fetchStoresForUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shops = shopApi.getShopsForUser(
                    userRepository.userId,
                    "Bearer ${userRepository.jwtToken}"
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

    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? = firebaseStorageRepository.uploadImageToFirebase(selectedImageUri)

    fun createShop(shop: ShopRequest, userId: String) =
        viewModelScope.launch {
            try {
                val shop = shopApi.createShop("Bearer ${userRepository.jwtToken}", shop)
                Log.d("SHOP", "${shop.id}")
                shopApi.shopAssignUser(shop.id.toString(), userId,"Bearer ${userRepository.jwtToken}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

}