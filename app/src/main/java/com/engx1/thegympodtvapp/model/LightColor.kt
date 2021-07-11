package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
    @SerializedName("updated_at")
    var updatedAt: String
): Serializable
