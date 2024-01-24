package com.example.api.product

import com.example.api.product.model.Product
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {

    @GET("/public/products")
    suspend fun getProducts(): List<Product>
}