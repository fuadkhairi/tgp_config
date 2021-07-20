package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class InstructorListResponse(
    @SerializedName("success")
    var success: String,
    @SerializedName("data")
    var data : ArrayList<Instructor>
)

data class Instructor(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("specialization")
    var specialization: String,
    @SerializedName("photo")
    var photo: String
)
