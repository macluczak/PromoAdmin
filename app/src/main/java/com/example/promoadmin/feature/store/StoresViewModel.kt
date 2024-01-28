package com.example.promoadmin.feature.store

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.firebase.FirebaseStorageRepository
import com.example.api.shop.ShopApi
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import com.example.promoadmin.repositories.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val shopApi: ShopApi,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {
    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> get() = _shop

    init {
        userRepository.getUserIdAndToken()
    }

    suspend fun editShop(shopReq: ShopRequest){
        shopApi.editShop(
        shopId = shop.value?.id.toString(),
            token = "Bearer ${userRepository.jwtToken}",
            request = shopReq
        )
    }

    fun fetchStoreDetails(shopId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shopDetails = shopApi.getShopDetails(shopId, "Bearer ${userRepository.jwtToken}")
                _shop.postValue(shopDetails)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? = firebaseStorageRepository.uploadImageToFirebase(selectedImageUri)

}