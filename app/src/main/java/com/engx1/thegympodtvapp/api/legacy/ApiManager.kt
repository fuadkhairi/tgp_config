package com.engx1.thegympodtvapp.api.legacy

import android.content.Context
import com.engx1.thegympodtvapp.model.MainWorkoutResponse
import com.engx1.thegympodtvapp.model.MoodColorListResponse

class ApiManager(private var mContext: Context?) : ApiClient(mContext) {

    fun getColorList(callBack: ApiCallBack<MoodColorListResponse>) {
        current(mContext, true).getColorList().enqueue(callBack)
    }


    fun getMainProgramme(callBack: ApiCallBack<MainWorkoutResponse>) {
        current(mContext, true).getMainProgramme().enqueue(callBack)
    }

//    fun acceptItemCollector(callBack: ApiCallBack<AcceptSettlementResponse>, jsonObject: JsonObject) {
//        current(mContext, true).acceptItemCollector(jsonObject).enqueue(callBack)
//    }
}


