package com.example.bookshopmanagement.data.model.request

import android.text.TextUtils

data class AuthorRequest(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var avatar: String? = null,
    var fileName: String? = null,
) {
    fun isFieldEmpty(): Boolean {
        return TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(avatar)
    }
}