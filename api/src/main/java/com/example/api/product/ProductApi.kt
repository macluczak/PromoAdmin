package com.example.api.product

import com.example.api.product.model.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("/public/products")
    suspend fun getProducts(): List<Product>
}