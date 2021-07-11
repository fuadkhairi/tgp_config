package com.engx1.thegympodtvapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoodColorList(
    @SerializedName("colors")
    var colors: ArrayList<LightColor>
) : Serializable