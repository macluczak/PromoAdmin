package com.example.api.firebase

import android.net.Uri

interface FirebaseStorageRepository {
    suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri?
}