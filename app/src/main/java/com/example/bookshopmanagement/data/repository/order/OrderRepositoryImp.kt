package com.example.bookshopmanagement.data.repository.order

import com.example.bookshopmanagement.data.model.OrderDetail
import com.example.bookshopmanagement.data.model.response.Message
import com.example.bookshopmanagement.data.model.response.order.OrderResponse
import com.example.bookshopmanagement.datasource.remote.IDataSource
import retrofit2.Response

class OrderRepositoryImp(private val iDataSource: IDataSource) : OrderRepository {
    override suspend fun getAllOrderByOrderStatus(orderStatusId: Int): Response<OrderResponse> {
        return iDataSource.getAllOrderByOrderStatus(orderStatusId)
    }

    override suspend fun updateOrderStatus(orderId: Int, orderStatusId: Int): Response<Message> {
        return iDataSource.updateOrderStatus(orderId, orderStatusId)
    }

    override suspend fun getOrdersByYear(year: Int): Response<OrderResponse> {
        return iDataSource.getOrdersByYear(year)
    }

    override suspend fun getOrderByMonthOfYear(year: Int, month: Int): Response<OrderResponse> {
        return iDataSource.getOrdersByMonthOfYear(year, month)
    }

    override suspend fun getOrderByToday(today: String): Response<OrderResponse> {
        return iDataSource.getOrderByToday(today)
    }

    override suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>? {
        return iDataSource.getOrderDetail(orderId)
    }
}