package com.example.bookshopmanagement.data.repository.user

import com.example.bookshopmanagement.data.model.response.AuthResponse
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.User
import com.example.bookshopmanagement.datasource.remote.IDataSource
import retrofit2.Response

class UserRepositoryImp(private val iDataSource: IDataSource) : UserRepository {
    override suspend fun login(email: String, password: String): Response<AuthResponse> {
        return iDataSource.login(email, password)
    }

    override suspend fun getAllCustomer(): Response<List<User>> {
        return iDataSource.getAllCustomer()
    }

    override suspend fun updateUserStatus(idUser: Int, status: String): Response<Message> {
        return iDataSource.updateUserStatus(idUser, status)
    }
}