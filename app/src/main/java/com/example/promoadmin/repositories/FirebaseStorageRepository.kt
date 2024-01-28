package com.example.promoadmin.repositories

import android.net.Uri
import com.example.api.firebase.FirebaseStorageRepository
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor() : FirebaseStorageRepository {

    override suspend fun uploadImageToFirebase(selectedImageUri: Uri?): Uri? {
        return try {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imagesRef: StorageReference? = storageRef.child("images")
            val riversRef = imagesRef?.child("images/${selectedImageUri?.lastPathSegment}")
            val uploadTask = selectedImageUri?.let { riversRef?.putFile(it)}

            uploadTask?.await()

            val downloadUrl = uploadTask?.result?.metadata?.reference?.downloadUrl?.await()

            downloadUrl
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}