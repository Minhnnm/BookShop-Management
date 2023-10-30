package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Supplier(
    @SerializedName("id")
    var id: Int?=null,
    @SerializedName("name")
    var name: String?=null,
): Serializable
