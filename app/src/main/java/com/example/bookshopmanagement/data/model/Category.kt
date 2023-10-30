package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("id") var categoryId: Int?=null,
    @SerializedName("name") var name: String?=null,
    @SerializedName("description") var description: String?=null,
): Serializable
