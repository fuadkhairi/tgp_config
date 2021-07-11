package com.engx1.thegympodtvapp.api.legacy

import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.MoodColorListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET(ApiService.GET_COLOR_LIST)
    fun getColorList(
        @Header("x-access-token") id: String = ApiService.X_ACCESS_TOKEN
    ): Call<MoodColorListResponse>
//
//    @POST(GET_BANK_ACCOUNT_INQUIRY)
//    fun getBankAccountInquiry(@Body jsonObject: JsonObject): Call<BankAccountInquiryResponse>
}

