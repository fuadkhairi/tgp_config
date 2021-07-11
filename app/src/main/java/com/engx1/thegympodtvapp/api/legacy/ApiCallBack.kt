package com.engx1.thegympodtvapp.api.legacy


import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class ApiCallBack<T>(
    private val apiListener: ApiResponseListener<T>,
    private val apiName: String,
    private val mContext: Context
) : Callback<T> {

    override fun onResponse(@NonNull call: Call<T>, @NonNull response: Response<T>) {
        when {
            response.code() == HttpURLConnection.HTTP_OK -> {
                apiListener.onApiSuccess(response.body()!!, apiName)
                println("response 200")
            }
            response.code() == HttpURLConnection.HTTP_BAD_REQUEST -> {

                val apiResponseError: String
                var responseData = ""
                try {
                    if (response.errorBody() != null) {
                        apiResponseError = response.errorBody()!!.string()
                        val jsonObject = JSONObject(apiResponseError)
                        responseData = jsonObject.getString("message")
                    } else {
                        apiResponseError = response.message()
                        val jsonObject = JSONObject(apiResponseError)
                        responseData = jsonObject.getString("message")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                apiListener.onApiError(responseData, apiName)
            }
            else -> {
                apiListener.onApiError(response.message(), apiName)
            }
        }
    }

    override fun onFailure(@NonNull call: Call<T>, @NonNull t: Throwable) {
        println("ApiFailure")
        Log.e("ERROR===>",t.toString())
        apiListener.onApiFailure(t.toString(), apiName)
    }
}