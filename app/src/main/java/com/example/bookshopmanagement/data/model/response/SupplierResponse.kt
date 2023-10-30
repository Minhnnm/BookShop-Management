package com.example.bookshopmanagement.data.model.response

import com.example.BookShopApp.data.model.Supplier
import com.google.gson.annotations.SerializedName

data class SupplierResponse(
    @SerializedName("count") var count: Int?,
    @SerializedName("rows")
    var suppliers: List<Supplier>,
)