package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateDataResponse(
    @SerializedName("title")
    var title: String,
    @SerializedName("version")
    var version: String,
    @SerializedName("change_log")
    var changeLog: ArrayList<String>
): Serializable
