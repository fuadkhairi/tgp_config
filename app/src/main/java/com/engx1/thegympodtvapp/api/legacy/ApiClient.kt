package com.engx1.thegympodtvapp.api.legacy

import android.annotation.SuppressLint
import android.content.Context
import com.engx1.thegympodtvapp.constant.AppUrlConstant
import com.engx1.thegympodtvapp.utils.SharedPrefManager
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class ApiClient internal constructor(context: Context?) {
    private val apiInterface: ApiInterface
    private var requestBuilder: Request.Builder? = null

    private val token = SharedPrefManager.getPreferenceString(context, "token").toString()

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.connectTimeout(120, TimeUnit.SECONDS)
        httpClient.readTimeout(120, TimeUnit.SECONDS)
        httpClient.writeTimeout(120, TimeUnit.SECONDS)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            requestBuilder = if (isTrue) {
                original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer $token")
                    .method(original.method, original.body)
            } else {
                original.newBuilder()
                    .header("Content-Type", "application/json")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .method(original.method, original.body)
            }
            val request = requestBuilder!!.build()
            chain.proceed(request)
        }

        val client = httpClient.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AppUrlConstant.BASE_URL.trim { it <= ' ' })
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var apiClient: ApiClient? = null
        private var isTrue: Boolean = false

        fun current(context: Context?, isTrue: Boolean): ApiInterface {
            Companion.isTrue = isTrue
            return if (isTrue) {
                getInstance(context, true).apiInterface
            } else {
                getInstance(context, false).apiInterface
            }
        }


        private fun getInstance(context: Context?, isTrue: Boolean): ApiClient {
            if (isTrue) {
                if (apiClient == null) {
                    apiClient =
                        ApiClient(context)
                }
            } else {
                if (apiClient == null) {
                    apiClient =
                        ApiClient(context)
                }
            }

            return apiClient as ApiClient
        }
    }

}
