package com.example.bookshopmanagement.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Author(
    @SerializedName("id") var authorId: Int? = null,
    @SerializedName("name") var authorName: String? = null,
    @SerializedName("description") var authorDescription: String? = null,
    @SerializedName("avatar") var authorAvatar: String? = null,
    var fileName: String? = null
) : Serializable
