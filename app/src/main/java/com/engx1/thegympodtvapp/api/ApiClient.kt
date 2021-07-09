package com.engx1.thegympodtvapp.api

import com.engx1.thegympodtvapp.BuildConfig
import com.engx1.thegympodtvapp.constant.AppUrlConstant
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else HttpLoggingInterceptor.Level.NONE
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            val original: Request = chain.request()
            builder.apply {
                header("Content-Type", "application/json")
                header("Connection", "close")
                cacheControl(CacheControl.FORCE_NETWORK)
                method(original.method, original.body)
            }
            chain.proceed(builder.build())
        })
        .readTimeout(2L, TimeUnit.MINUTES)
        .connectTimeout(1L, TimeUnit.MINUTES)
        .writeTimeout(1L, TimeUnit.MINUTES)
        .pingInterval(30L, TimeUnit.SECONDS)
        .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppUrlConstant.BASE_URL.trim())
            .client(client)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API_SERVICE: ApiService = getRetrofit().create(ApiService::class.java)
}