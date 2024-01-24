package com.example.api.shop

import com.example.api.product.model.Product
import com.example.api.shop.model.Shop
import retrofit2.http.GET

interface ShopApi {

    @GET("/public/shops")
    suspend fun getShops(): List<Shop>
}