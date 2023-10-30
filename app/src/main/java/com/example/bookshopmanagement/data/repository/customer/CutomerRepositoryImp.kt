package com.example.bookshopmanagement.data.repository.customer

import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.datasource.remote.IDataSource
import retrofit2.Response

class CutomerRepositoryImp(private val iDataSource: IDataSource) : CustomerRepository {
    override suspend fun getCustomerNumber(): Response<Message> {
        return iDataSource.getCustomerNumber()
    }
}