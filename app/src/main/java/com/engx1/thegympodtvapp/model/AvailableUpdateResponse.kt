package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class AvailableUpdateResponse(
    @SerializedName("type")
    var type: String,
    @SerializedName("data")
    var data: ArrayList<UpdateDataResponse>,
    @SerializedName("download_url")
    var downloadUrl: String
)
