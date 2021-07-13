package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class AvailableMusicResponse(
    @SerializedName("type")
    var type: String,
    @SerializedName("data")
    var data: ArrayList<MusicResponse>
)
