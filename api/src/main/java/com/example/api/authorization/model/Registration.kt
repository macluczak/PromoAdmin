package com.example.api.authorization.model
data class RegistrationRequest(
    val email: String,
    val password: String,
    val role: String? = "moderator"
)
