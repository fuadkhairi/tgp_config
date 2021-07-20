package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data: LoginData
)

data class LoginData(
    @SerializedName("token")
    var token: String,
    @SerializedName("user")
    var user: LoginUser
)

data class LoginUser(
    @SerializedName("isPersonalTrainer")
    var isPersonalTrainer: Boolean
)
