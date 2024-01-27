package com.example.promoadmin.ui.home

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
class HomeViewModel  @Inject constructor(
    private val shopApi: ShopApi,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    init {
        userRepository.getUserIdAndToken()
    }

    fun fetchStoresForUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shops = shopApi.getShopsForUser(userRepository.userId, "Bearer ${userRepository.jwtToken}")
                _shops.postValue(shops)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}