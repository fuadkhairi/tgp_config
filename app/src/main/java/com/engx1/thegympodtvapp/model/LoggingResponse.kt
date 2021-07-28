package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class LoggingResponse(
    @SerializedName("data")
    var data: LoggingData,
    @SerializedName("meta")
    var meta: LoggingMeta
)

data class LoggingData(
    @SerializedName("identifier")
    var identifier: Int?,
    @SerializedName("watchDuration")
    var watchDuration: Int?
)

data class LoggingMeta(
    @SerializedName("message")
    var message: String
)
