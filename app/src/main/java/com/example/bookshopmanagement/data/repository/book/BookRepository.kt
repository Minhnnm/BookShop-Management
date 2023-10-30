package com.example.bookshopmanagement.data.repository.book

import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.product.ProductResponse
import retrofit2.Response

interface BookRepository {
    suspend fun getProducts(limit: Int, page: Int, description: Int): Response<ProductResponse>
    suspend fun getSearchProducts(
        limit: Int,
        page: Int,
        description: Int,
        query: String
    ): Response<ProductResponse>

    suspend fun addBook(productRequest: ProductRequest): Response<Message>

    suspend fun updateBook(productRequest: ProductRequest): Response<Message>

    suspend fun deleteBook(productId: Int): Response<Message>

    suspend fun getBookDetail(productId: Int): Response<ProductRequest>

    suspend fun getBookBestSeller(): Response<ProductResponse>
}