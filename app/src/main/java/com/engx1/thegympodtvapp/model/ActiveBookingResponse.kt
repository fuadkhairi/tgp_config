package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class ActiveBookingResponse(
    @SerializedName("success")
    var success: String?,
    @SerializedName("data")
    var data: BookingData?
)

data class BookingData(
    @SerializedName("data")
    var data: ArrayList<User>?,
    @SerializedName("current_time")
    var currentTime: String?,
    @SerializedName("cleaning_message")
    var cleaningMessage: String?
)

data class User(
    @SerializedName("first_name")
    var firstName: String
)
