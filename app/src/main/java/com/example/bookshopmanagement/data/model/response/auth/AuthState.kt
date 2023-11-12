package com.example.bookshopmanagement.data.model.response
import com.example.bookshopmanagement.data.model.response.error.Error
data class AuthState(
    val error: Error,
    val loginResponse: AuthResponse?,
)
