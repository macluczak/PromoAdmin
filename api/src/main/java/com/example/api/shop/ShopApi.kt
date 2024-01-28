package com.example.api.shop

import com.example.api.authorization.model.RegistrationRequest
import com.example.api.product.model.Product
import com.example.api.shop.model.Shop
import com.example.api.shop.model.ShopRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopApi {

    @GET("/public/shops")
    suspend fun getShops(): List<Shop>

    @GET("/shops/{id}")
    suspend fun getShopDetails(
        @Path("id") shopId: String,
        @Header("Authorization") token: String,
    ): Shop

    @POST("/shops")
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: ShopRequest,
    ): Shop

    @POST("/shops/{shopId}/assignUser/{userId}")
    suspend fun shopAssignUser(
        @Path("shopId") shopId: String,
        @Path("userId") userId: String,
        @Header("Authorization") token: String,
    ): List<Shop>

    @GET("/shops/forUser/{id}")
    suspend fun getShopsForUser(
        @Path("id") userId: String,
        @Header("Authorization") token: String,
    ): List<Shop>

    @PUT("/shops/{id}")
    suspend fun editShop(
        @Path("id") shopId: String,
        @Header("Authorization") token: String,
        @Body request: ShopRequest
    ): Shop

}