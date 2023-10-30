package com.example.bookshopmanagement.data.repository.supplier

import com.example.bookshopmanagement.data.model.request.SupplierRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.SupplierResponse
import com.example.bookshopmanagement.datasource.remote.IDataSource
import retrofit2.Response

class SupplierRepositoryImp(private val iDataSource: IDataSource) : SupplierRepository {
    override suspend fun getSuppliers(): Response<SupplierResponse> {
        return iDataSource.getSuppliers()
    }

    override suspend fun addSupplier(supplier: SupplierRequest): Response<Message> {
        return iDataSource.addSupplier(supplier)
    }
}