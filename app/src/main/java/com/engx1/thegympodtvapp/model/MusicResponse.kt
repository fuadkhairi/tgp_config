package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class MusicResponse(
    @SerializedName("playlist_name")
    var title: String,
    @SerializedName("vendor")
    var vendor: String,
    @SerializedName("platlist_link")
    var streamUrl: String
)
