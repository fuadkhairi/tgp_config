package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class MusicResponse(
    @SerializedName("title")
    var title: String,
    @SerializedName("genre")
    var genres: String,
    @SerializedName("stream_url")
    var streamUrl: String
)
