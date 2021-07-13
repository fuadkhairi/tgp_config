package com.engx1.thegympodtvapp.api.legacy

import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.AvailableMusicResponse
import com.engx1.thegympodtvapp.model.AvailableUpdateResponse
import com.engx1.thegympodtvapp.model.MainWorkoutResponse
import com.engx1.thegympodtvapp.model.MoodColorListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiInterface {
    @GET(ApiService.GET_COLOR_LIST)
    fun getColorList(
        @Header("x-access-token") id: String = ApiService.X_ACCESS_TOKEN
    ): Call<MoodColorListResponse>

    @GET(ApiService.GET_MAIN_PROGRAMME)
    fun getMainProgramme(
        @Header("x-access-token") id: String = ApiService.X_ACCESS_TOKEN
    ): Call<MainWorkoutResponse>

    @GET(ApiService.GET_AVAILABLE_MUSIC)
    fun getAvailableMusic(): Call<AvailableMusicResponse>

    @GET(ApiService.GET_AVAILABLE_UPDATE)
    fun getAvailableUpdate(): Call<AvailableUpdateResponse>
//
//    @POST(GET_BANK_ACCOUNT_INQUIRY)
//    fun getBankAccountInquiry(@Body jsonObject: JsonObject): Call<BankAccountInquiryResponse>
}

