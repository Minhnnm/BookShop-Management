package com.example.bookshopmanagement.data.model.response.category

import com.example.BookShopApp.data.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("count") var count: Int?,
    @SerializedName("rows")
    var categories: List<Category>,
)