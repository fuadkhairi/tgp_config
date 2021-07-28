package com.engx1.thegympodtvapp.api.legacy

import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

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
    fun getAvailableMusic(@Header("x-access-token") id: String = ApiService.X_ACCESS_TOKEN
    ): Call<AvailableMusicResponse>

    @GET(ApiService.GET_AVAILABLE_UPDATE)
    fun getAvailableUpdate(): Call<AvailableUpdateResponse>

    @GET("${ApiService.GET_INSTRUCTOR}/{id}")
    fun getInstructorDetail(@Path("id") id: Int): Call<InstructorDetailResponse>

    @GET(ApiService.GET_VIMEO_VIDEO)
    fun getVimeoVideoConfig(@Path("VIMEO_ID") vimeoId: String): Call<VimeoConfigResponse>

    @PUT(ApiService.LOG_VIDEO)
    fun logStartVideo(@Body jsonObject: JsonObject): Call<Any>

    @PATCH(ApiService.LOG_VIDEO)
    fun logEndVideo(@Body jsonObject: JsonObject): Call<Any>

//
//    @POST(GET_BANK_ACCOUNT_INQUIRY)
//    fun getBankAccountInquiry(@Body jsonObject: JsonObject): Call<BankAccountInquiryResponse>
}

