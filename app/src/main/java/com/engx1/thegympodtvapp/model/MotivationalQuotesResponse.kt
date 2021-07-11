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
