package com.engx1.thegympodtvapp.api

import com.engx1.thegympodtvapp.model.*
import com.google.gson.JsonObject
import retrofit2.http.*

interface ApiService {
    companion object {
        const val X_ACCESS_TOKEN = "e4c5135f5764a353b5b491687cf6800d"
        const val GET_MOTIVATIONAL_QUOTES = "tv/motivational_quotes"
        const val GET_ACTIVE_BOOKINGS = "tv/bookings/{time}/{gympod_id}"
        const val GET_COLOR_LIST = "tv/control/colors"
        const val SET_LIGHT_STATE = "tv/control/lights"
        const val SET_MOOD_STATE = "tv/control/striplights"
        const val SET_MOOD_COLOR = "tv/control/color/striplights"
        const val GET_MAIN_PROGRAMME = "tv/programme/main"
    }

    @GET(GET_MOTIVATIONAL_QUOTES)
    suspend fun getMotivationQuotes(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN): MotivationalQuotesResponse

    @GET(GET_ACTIVE_BOOKINGS)
    suspend fun getActiveBookings(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
    @Path("time") time: String, @Path("gympod_id") gympodId: String): ActiveBookingResponse

    @GET(GET_COLOR_LIST)
    fun getColorList(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN): MoodColorListResponse

    @POST(SET_LIGHT_STATE)
    suspend fun setLightState(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN, @Body jsonObject: JsonObject): LightStateResponse

    @POST(SET_MOOD_STATE)
    suspend fun setMoodState(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN, @Body jsonObject: JsonObject): MoodStateResponse

    @POST(SET_MOOD_COLOR)
    suspend fun setActiveMoodColor(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN, @Body jsonObject: JsonObject): MoodStateResponse
}