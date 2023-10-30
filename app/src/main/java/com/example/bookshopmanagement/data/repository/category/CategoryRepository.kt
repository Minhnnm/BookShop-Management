package com.example.bookshopmanagement.data.repository.category

import com.example.bookshopmanagement.data.model.request.CategoryRequest
import com.example.bookshopmanagement.data.model.response.category.CategoryResponse
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.category.CategoryBestSeller
import retrofit2.Response

interface CategoryRepository {
    suspend fun getCategroies(): Response<CategoryResponse>

    suspend fun addCategory(category: CategoryRequest): Response<Message>

    suspend fun getCategoryBestSeller(): Response<List<CategoryBestSeller>>
}