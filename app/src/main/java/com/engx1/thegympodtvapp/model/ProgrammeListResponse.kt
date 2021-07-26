package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName

data class ProgrammeListResponse(
    @SerializedName("success")
    var success: String?,
    @SerializedName("data")
    var data : ProgrammeList?
)

data class ProgrammeList(
    @SerializedName("subscription_status")
    var subscriptionStatus: Boolean?,
    @SerializedName("data")
    var data: ArrayList<InstructorProgramme>?
)

