package com.example.promoadmin.feature.store

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.firebase.FirebaseStorageRepository
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import com.example.api.user.model.User
import com.example.promoadmin.repositories.ShopRepository
import com.example.promoadmin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {
    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> get() = _shop


    private val _userData = MutableLiveData<User>()

    val userData: LiveData<User> get() = _userData


    init {
        userRepository.getUserIdAndToken()
    }

    suspend fun editShop(shopReq: ShopRequest){
        shopRepository.editShop(
        shopId = shop.value?.id.toString(),
            token = userRepository.jwtToken,
            request = shopReq
        )
    }

    fun getUser() {
        viewModelScope.launch {
            val user = userRepository.getUserData()
            _userData.value = user
        }
    }

    fun fetchStoreDetails(shopId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shopDetails = shopRepository.getShopDetails(shopId, userRepository.jwtToken)
                _shop.postValue(shopDetails)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteStore(shopId: String) = viewModelScope.launch {
        shopRepository.deleteShop(shopId, userRepository.jwtToken)
    }

    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? = firebaseStorageRepository.uploadImageToFirebase(selectedImageUri)

}