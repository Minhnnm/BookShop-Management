package com.example.bookshopmanagement.data.repository.customer

import com.example.bookshopmanagement.data.model.response.Message
import retrofit2.Response

interface CustomerRepository {
    suspend fun getCustomerNumber(): Response<Message>
}