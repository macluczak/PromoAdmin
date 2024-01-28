package com.example.promoadmin.feature.product

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.firebase.FirebaseStorageRepository
import com.example.api.product.model.ProductRequest
import com.example.promoadmin.repositories.ProductRepository
import com.example.promoadmin.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository,
) : ViewModel() {

    init {
        userRepository.getUserIdAndToken()
    }

    fun editProduct(productRequest: ProductRequest) = viewModelScope.launch {
        productRepository.editProduct(
            id = productRequest.id.toString(),
            jwtToken = userRepository.jwtToken,
            product = productRequest,
        )
    }

    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? =
        firebaseStorageRepository.uploadImageToFirebase(selectedImageUri)

}