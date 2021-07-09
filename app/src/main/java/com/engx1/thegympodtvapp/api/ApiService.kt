package com.engx1.thegympodtvapp.api

import com.engx1.thegympodtvapp.model.MotivationalQuotesResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    companion object {
        const val X_ACCESS_TOKEN = "e4c5135f5764a353b5b491687cf6800d"
        const val GET_MOTIVATIONAL_QUOTES = "tv/motivational_quotes"
        const val GET_BOOKINGS = "tv/bookings/{time}/{gympod_id}"
        const val GET_COLOR_LIST = "tv/control/colors"
    }

    @GET(GET_MOTIVATIONAL_QUOTES)
    suspend fun getMotivationQuotes(@Header("x-access-token") accessToken: String = X_ACCESS_TOKEN): MotivationalQuotesResponse

}