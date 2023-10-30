package com.example.bookshopmanagement.data.model.response.author

import com.example.bookshopmanagement.data.model.Author
import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("authors") var authors: List<Author>,
)