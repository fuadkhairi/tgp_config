package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoodColorListResponse(
    @SerializedName("success")
    var successString: String,
    @SerializedName("data")
    var data: MoodColorList
) : Serializable
