package com.example.bookshopmanagement.data.model.response.author

import com.example.bookshopmanagement.data.model.Author
import com.google.gson.annotations.SerializedName

data class AuthorInfor(
    @SerializedName("result" ) var author : Author,
)