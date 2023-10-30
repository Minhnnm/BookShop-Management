package com.example.bookshopmanagement.data.model.response.category

import com.example.BookShopApp.data.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryBestSeller(
    @SerializedName("category") var category: Category,
    @SerializedName("totalSold") var totalSold: Long,
)
