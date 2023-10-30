package com.example.bookshopmanagement.data.model.response.product

import com.example.bookshopmanagement.data.model.Product

data class ProductState (
    val products: List<Product>?,
    val isDefaultState: Boolean
)