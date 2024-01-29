package com.example.api.product

import com.example.api.product.model.Product
import com.example.api.product.model.ProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {

    @GET("/public/products")
    suspend fun getProducts(): List<Product>

    @PUT("/products/{id}")
    suspend fun editProduct(
        @Path("id") productId: String,
        @Header("Authorization") token: String,
        @Body request: ProductRequest
    ): Product

    @POST("/products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body request: ProductRequest
    ): Product

    @DELETE("/products/{id}")
    suspend fun deleteProduct(
        @Path("id") productId: String,
        @Header("Authorization") token: String,
    ): Response<Void>
}