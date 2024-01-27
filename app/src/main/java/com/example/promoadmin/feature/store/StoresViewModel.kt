package com.example.promoadmin.feature.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.shop.ShopApi
import com.example.api.shop.model.Shop
import com.example.promoadmin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoresViewModel @Inject constructor(
    private val shopApi: ShopApi,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _shop = MutableLiveData<Shop>()
    val shop: LiveData<Shop> get() = _shop

    init {
        userRepository.getUserIdAndToken()
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
}