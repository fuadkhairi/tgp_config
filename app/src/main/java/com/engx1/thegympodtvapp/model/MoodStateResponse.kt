package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class MoodStateResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data : ArrayList<Any>
)
