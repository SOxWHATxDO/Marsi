package com.example.marsi.model

data class User(
    val id: String,
    val login: String,
    val role: UserRole,
    val token: String
)
