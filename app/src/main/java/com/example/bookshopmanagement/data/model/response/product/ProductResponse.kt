package com.example.bookshopmanagement.data.model.response.product

import com.example.bookshopmanagement.data.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("rows")
    var products: List<Product>,
)
