package com.example.promoadmin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.shop.ShopApi
import com.example.api.shop.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(
    private val shopApi: ShopApi
) : ViewModel() {
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    init {
        fetchStores()
    }

    fun fetchStores() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = shopApi.getShops()
                _shops.postValue(response)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}