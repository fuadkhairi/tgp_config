package com.engx1.thegympodtvapp.api

import com.engx1.thegympodtvapp.BuildConfig
import com.engx1.thegympodtvapp.model.*
import com.google.gson.JsonObject
import retrofit2.http.*

interface ApiService {
    companion object {
        const val X_ACCESS_TOKEN = "e4c5135f5764a353b5b491687cf6800d"
        const val GET_MOTIVATIONAL_QUOTES = "tv/motivational_quotes"
        const val GET_ACTIVE_BOOKINGS = "tv/bookings/{time}/{gympod_id}"
        const val GET_BOOKING_PROGRAMME = "tv/programme/home"
        const val GET_COLOR_LIST = "tv/control/colors"
        const val SET_LIGHT_STATE = "tv/control/lights"
        const val SET_MOOD_STATE = "tv/control/striplights"
        const val SET_MOOD_COLOR = "tv/control/color/striplights"
        const val GET_MAIN_PROGRAMME = "tv/programme/main"
        const val GET_AVAILABLE_MUSIC = "tv/programme/playlist"
        const val GET_AVAILABLE_UPDATE =
            "https://raw.githubusercontent.com/fuadkhairi/tgp_config/master/app_update_config.json"
        const val GET_ACADEMY_MAIN_PROGRAMME = "workout/main"
        const val GET_INSTRUCTOR = "workout/instructor"
        const val USER_LOGIN = "login"
        const val GET_PROGRAMME_LIST = "workout/programme"
    }

    @POST(USER_LOGIN)
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("device_id") deviceId: String,
        @Query("platform") platform: String,
        @Query("version") version: String
    ) : LoginResponse

    @POST(GET_PROGRAMME_LIST)
    suspend fun getProgrammeList(@Header("Authorization") bearerToken: String, @Query("main_category_id") mainCategoryId: Int): ProgrammeListResponse

    @GET("$GET_INSTRUCTOR/{id}")
    suspend fun getInstructorDetail(@Header("Authorization") bearerToken: String, @Path("id") id: Int): InstructorDetailResponse

    @POST(GET_INSTRUCTOR)
    suspend fun getInstructor(@Header("Authorization") bearerToken: String, @Query("limit") limit: Int): InstructorListResponse

    @GET(GET_ACADEMY_MAIN_PROGRAMME)
    suspend fun getAcademyMainProgramme(@Header("Authorization") bearerToken: String): MainWorkoutResponse

    @GET(GET_MOTIVATIONAL_QUOTES)
    suspend fun getMotivationQuotes(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN): MotivationalQuotesResponse

    @GET(GET_BOOKING_PROGRAMME)
    suspend fun getBookingProgramme(
        @Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
        @Query("gympod_id") gympodId: String, @Query("location") location: String
    ): BookingProgrammeResponse

    @GET(GET_ACTIVE_BOOKINGS)
    suspend fun getActiveBookings(
        @Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
        @Path("time") time: String, @Path("gympod_id") gympodId: String
    ): ActiveBookingResponse

    @GET(GET_COLOR_LIST)
    fun getColorList(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN): MoodColorListResponse

    @POST(SET_LIGHT_STATE)
    suspend fun setLightState(
        @Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
        @Body jsonObject: JsonObject
    ): LightStateResponse

    @POST(SET_MOOD_STATE)
    suspend fun setMoodState(
        @Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
        @Body jsonObject: JsonObject
    ): MoodStateResponse

    @POST(SET_MOOD_COLOR)
    suspend fun setActiveMoodColor(
        @Header("x-access-token") accessToken: String = X_ACCESS_TOKEN,
        @Body jsonObject: JsonObject
    ): MoodStateResponse
}