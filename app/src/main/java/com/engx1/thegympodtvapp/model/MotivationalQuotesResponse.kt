package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class MotivationalQuotesResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data: Quotes
)

data class Quotes(
    @SerializedName("text")
    var text: String,
    @SerializedName("author")
    var author: String
)

data class MotivationalData(
    @SerializedName("colors")
    var colors: ArrayList<LightColor>
)

data class LightColor(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("hex_code")
    var hexCode: String,
    @SerializedName("strip_code")
    var stripCode: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("created_at")
    var createdAt: String,
    @SerializedName("created_at")
    var updatedAt: String
)