package com.example.api.authorization.model

import com.example.api.user.model.User
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val user: User,
    val value: String,
    val id: String,
    val createdAt: String
)
