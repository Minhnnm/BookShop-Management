package com.example.bookshopmanagement.data.model.response

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    var userId: Int? = null,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("new_pass")
    var newPassword: String = "",
    @SerializedName("password_again")
    var passwordAgain: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("shipping_region_id")
    var shippingRegionId: Int? = null,
    @SerializedName("mob_phone")
    var mobPhone: String = "",
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("date_of_birth")
    var dateOfBirth: String? = null,
    @SerializedName("avatar")
    var avatar: String? = null,
    @SerializedName("role")
    var role: String? = null,
    @SerializedName("status")
    var status: String? = null,
)

