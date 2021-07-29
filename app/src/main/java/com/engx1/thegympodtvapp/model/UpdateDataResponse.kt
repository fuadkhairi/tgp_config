package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateDataResponse(
    @SerializedName("title")
    var title: String,
    @SerializedName("version")
    var version: String,
    @SerializedName("update_all_gympod")
    var updateAllGympod: Boolean,
    @SerializedName("update_by_id")
    var updateById: ArrayList<String>,
    @SerializedName("download_url")
    var downloadUrl: String,
    @SerializedName("change_log")
    var changeLog: ArrayList<String>
): Serializable
