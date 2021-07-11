package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class MainWorkoutResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data: ArrayList<MainProgramme>
)

data class MainProgramme(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("is_active")
    var isActive: String,
    @SerializedName("banner")
    var banner: String
)