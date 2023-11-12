package com.example.bookshopmanagement.datasource.remote

import com.example.bookshopmanagement.data.model.OrderDetail
import com.example.bookshopmanagement.data.model.request.AuthorRequest
import com.example.bookshopmanagement.data.model.request.CategoryRequest
import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.data.model.request.SupplierRequest
import com.example.bookshopmanagement.data.model.response.*
import com.example.bookshopmanagement.data.model.response.author.AuthorInfor
import com.example.bookshopmanagement.data.model.response.author.AuthorResponse
import com.example.bookshopmanagement.data.model.response.category.CategoryBestSeller
import com.example.bookshopmanagement.data.model.response.category.CategoryResponse
import com.example.bookshopmanagement.data.model.response.order.OrderResponse
import com.example.bookshopmanagement.data.model.response.product.ProductResponse
import retrofit2.Response

interface IDataSource {

    suspend fun login(email: String, password: String): Response<AuthResponse>
    suspend fun getAllCustomer(): Response<List<User>>

    suspend fun updateUserStatus(idUser: Int, status: String): Response<Message>
    suspend fun getProducts(limit: Int, page: Int, description: Int): Response<ProductResponse>
    suspend fun getSearchProducts(
        limit: Int,
        page: Int,
        description: Int,
        query: String,
    ): Response<ProductResponse>

    suspend fun addBook(productRequest: ProductRequest): Response<Message>

    suspend fun updateBook(productRequest: ProductRequest): Response<Message>

    suspend fun deleteBook(productId: Int): Response<Message>

    suspend fun getBookDetail(productId: Int): Response<ProductRequest>

    suspend fun getBookBestSeller(): Response<ProductResponse>

    suspend fun getAuthors(limit: Int, page: Int): Response<AuthorResponse>

    suspend fun getAuthor(authorId: Int): Response<AuthorInfor>?

    suspend fun addAuthor(author: AuthorRequest): Response<Message>

    suspend fun getCategories(): Response<CategoryResponse>

    suspend fun getCategoryBestSeller(): Response<List<CategoryBestSeller>>

    suspend fun addCategory(category: CategoryRequest): Response<Message>

    suspend fun getSuppliers(): Response<SupplierResponse>

    suspend fun addSupplier(supplier: SupplierRequest): Response<Message>

    suspend fun getAllOrderByOrderStatus(orderStatusId: Int): Response<OrderResponse>

    suspend fun updateOrderStatus(orderId: Int, orderStatusId: Int): Response<Message>

    suspend fun getOrdersByYear(year: Int): Response<OrderResponse>

    suspend fun getOrdersByMonthOfYear(year: Int, month: Int): Response<OrderResponse>

    suspend fun getOrderByToday(today: String): Response<OrderResponse>

    suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>?
}