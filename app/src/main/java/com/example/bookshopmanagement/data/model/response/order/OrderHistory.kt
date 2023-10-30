package com.example.bookshopmanagement.data.model.response.order

import com.example.bookshopmanagement.data.model.Order

data class OrderHistory(
    val header: String?,
    val order: Order?,
)