package com.example.bookshopmanagement.data.api

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
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("customers/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<AuthResponse>

    @GET("/customers/all")
    suspend fun getAllCustomer(): Response<List<User>>

    @FormUrlEncoded
    @PUT("/customers/update/status")
    suspend fun updateUserStatus(
        @Field("idUser") idUser: Int,
        @Field("status") status: String
    ): Response<Message>

    @GET("/products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
    ): Response<ProductResponse>

    @GET("products/search")
    suspend fun getSearchProducts(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("description_length") descriptionLength: Int,
        @Query("query_string") queryString: String,
        @Query("filter_type") filterType: Int,
        @Query("price_sort_order") priceSortOrder: String,
    ): Response<ProductResponse>

    @POST("products/add")
    suspend fun addBook(@Body productRequest: ProductRequest): Response<Message>

    @GET("products/detail/{product_id}")
    suspend fun getBookDetail(@Path("product_id") productId: Int): Response<ProductRequest>

    @PUT("products/update")
    suspend fun updateBook(@Body productRequest: ProductRequest): Response<Message>

    @DELETE("products/delete/{product_id}")
    suspend fun deleteBook(@Path("product_id") productId: Int): Response<Message>

    @GET("products/bestseller")
    suspend fun getBookBestSeller(): Response<ProductResponse>


    @GET("author/all")
    suspend fun getAllAuthor(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<AuthorResponse>

    @POST("author/add")
    suspend fun addAuthor(@Body author: AuthorRequest): Response<Message>

    @GET("author/{authorId}")
    suspend fun getAuthor(@Path("authorId") authorId: Int): Response<AuthorInfor>

    @GET("category")
    suspend fun getAllCategory(): Response<CategoryResponse>

    @POST("category/add")
    suspend fun addCategory(@Body category: CategoryRequest): Response<Message>

    @GET("category/bestseller")
    suspend fun getCategoryBestSeller(): Response<List<CategoryBestSeller>>

    @GET("supplier")
    suspend fun getAllSuppliers(): Response<SupplierResponse>

    @POST("supplier/add")
    suspend fun addSupplier(@Body supplier: SupplierRequest): Response<Message>

    @GET("orders/status/{orderStatusId}")
    suspend fun getAllOrderByOrderStatus(@Path("orderStatusId") orderStatusId: Int): Response<OrderResponse>

    @PUT("orders/status")
    suspend fun updateOrderStatus(
        @Query("orderId") orderId: Int,
        @Query("orderStatusId") orderStatusId: Int
    ): Response<Message>

    @GET("orders/year/{year}")
    suspend fun getOrderByYears(@Path("year") year: Int): Response<OrderResponse>

    @GET("orders/monthOfYear")
    suspend fun getOrderByMonthOfYear(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<OrderResponse>

    @GET("orders/today")
    suspend fun getOrderByToday(@Query("today") today: String): Response<OrderResponse>

    @GET("orders/detail/{orderId}")
    suspend fun getOrderDetail(@Path("orderId") orderId: Int): Response<OrderDetail>
}