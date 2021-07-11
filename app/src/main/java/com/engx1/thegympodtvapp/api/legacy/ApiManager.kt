package com.engx1.thegympodtvapp.api.legacy

import android.annotation.SuppressLint
import android.content.Context
import com.engx1.thegympodtvapp.model.MoodColorListResponse

class ApiManager(private var mContext: Context?) : ApiClient(mContext) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var apiManager: ApiManager? = null

        fun getInstance(context: Context): ApiManager {
            if (apiManager == null) {
                apiManager =
                    ApiManager(context)
            }
            return apiManager as ApiManager
        }
    }

    fun getColorList(callBack: ApiCallBack<MoodColorListResponse>) {
        current(mContext, true).getColorList().enqueue(callBack)
    }

//    fun acceptItemCollector(callBack: ApiCallBack<AcceptSettlementResponse>, jsonObject: JsonObject) {
//        current(mContext, true).acceptItemCollector(jsonObject).enqueue(callBack)
//    }
}


