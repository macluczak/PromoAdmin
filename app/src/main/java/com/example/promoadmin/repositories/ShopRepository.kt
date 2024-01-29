package com.example.promoadmin.repositories

import com.example.api.shop.ShopApi
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import retrofit2.Response
import javax.inject.Inject

class ShopRepository @Inject constructor(private val shopApi: ShopApi) {

    suspend fun getShops(): List<Shop> {
        return shopApi.getShops()
    }

    suspend fun getShopDetails(shopId: String, token: String): Shop {
        return shopApi.getShopDetails(shopId, "Bearer $token")
    }

    suspend fun createShop(token: String, request: ShopRequest): Shop {
        return shopApi.createShop("Bearer $token", request)
    }

    suspend fun shopAssignUser(shopId: String, userId: String, token: String): List<Shop> {
        return shopApi.shopAssignUser(shopId, userId, "Bearer $token")
    }

    suspend fun getShopsForUser(userId: String, token: String): List<Shop> {
        return shopApi.getShopsForUser(userId, "Bearer $token")
    }

    suspend fun editShop(shopId: String, token: String, request: ShopRequest): Shop {
        return shopApi.editShop(shopId, "Bearer $token", request)
    }

    suspend fun deleteShop(shopId: String, token: String): Response<Void> {
        return shopApi.deleteShop(shopId, "Bearer $token")
    }
}