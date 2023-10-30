package com.example.bookshopmanagement.data.repository.order

import com.example.bookshopmanagement.data.model.OrderDetail
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.order.OrderResponse
import retrofit2.Response

interface OrderRepository {
    suspend fun getAllOrderByOrderStatus(orderStatusId: Int): Response<OrderResponse>

    suspend fun updateOrderStatus(orderId: Int, orderStatusId: Int): Response<Message>

    suspend fun getOrdersByYear(year: Int): Response<OrderResponse>

    suspend fun getOrderByMonthOfYear(year: Int, month: Int): Response<OrderResponse>

    suspend fun getOrderByToday(today: String): Response<OrderResponse>

    suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>?
}