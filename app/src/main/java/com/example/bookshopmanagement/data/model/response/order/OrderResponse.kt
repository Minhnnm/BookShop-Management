package com.example.bookshopmanagement.data.model.response.order

import com.example.bookshopmanagement.data.model.Order
import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("count") var count: Int,
    @SerializedName("orders") var orders: List<Order>,
)
