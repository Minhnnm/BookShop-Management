package com.example.bookshopmanagement.data.repository.user

import com.example.bookshopmanagement.data.model.response.AuthResponse
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.User
import retrofit2.Response

interface UserRepository {
    suspend fun login(email: String, password: String): Response<AuthResponse>
    suspend fun changePassword(
        email: String, old_password: String,
        new_password: String,
    ): Response<User>?
    suspend fun getUser(): Response<User>?
    suspend fun getAllCustomer(): Response<List<User>>
    suspend fun updateUserStatus(idUser: Int, status: String): Response<Message>
}