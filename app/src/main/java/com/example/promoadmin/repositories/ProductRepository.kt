package com.example.promoadmin.repositories

import com.example.api.product.ProductApi
import com.example.api.product.model.Product
import com.example.api.product.model.ProductRequest
import retrofit2.Response
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productApi: ProductApi) {

    suspend fun editProduct(id: String, jwtToken: String, product: ProductRequest): Product {
        return productApi.editProduct(productId = id, token = "Bearer $jwtToken", request = product)
    }

    suspend fun addProduct(jwtToken: String, product: ProductRequest): Product {
        return productApi.addProduct(token = "Bearer $jwtToken", request = product)
    }

    suspend fun deleteProduct(id: String, jwtToken: String): Response<Void> {
        return productApi.deleteProduct(productId = id, token = "Bearer $jwtToken")
    }
}