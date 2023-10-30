package com.example.bookshopmanagement.data.repository.supplier

import com.example.bookshopmanagement.data.model.request.SupplierRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.SupplierResponse
import retrofit2.Response

interface SupplierRepository {
    suspend fun getSuppliers(): Response<SupplierResponse>
    suspend fun addSupplier(supplier: SupplierRequest): Response<Message>
}