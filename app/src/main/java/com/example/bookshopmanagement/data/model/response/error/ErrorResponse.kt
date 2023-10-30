package com.example.bookshopmanagement.data.model.response.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: Error,
)
