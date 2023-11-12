package com.example.bookshopmanagement.data.model.response

import android.text.TextUtils
import android.util.Patterns
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("customer") val user: User,
    @SerializedName("expires_in") val expiresIn: String = "",
) {
    fun isSignInFieldEmpty(): Boolean {
        return TextUtils.isEmpty(user.email) || TextUtils.isEmpty(user.password)
    }

    fun isValidEmail(): Boolean {
        return user.email.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }

    fun isPasswordGreaterThan4(): Boolean {
        return user.password.length >= 5
    }
}
