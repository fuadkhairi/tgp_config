package com.engx1.thegympodtvapp.api.legacy

import android.content.Context
import com.engx1.thegympodtvapp.model.*
import com.google.gson.JsonObject
import org.json.JSONObject

class ApiManager(private var mContext: Context?) : ApiClient(mContext) {

    fun getIndividualInstructor(callBack: ApiCallBack<InstructorDetailResponse>, id: Int) {
        current(mContext, true).getInstructorDetail(id).enqueue(callBack)
    }

    fun startLoggingVideo(callBack: ApiCallBack<LoggingResponse>, jsonObject: JsonObject) {
        current(mContext, true).logStartVideo(jsonObject).enqueue(callBack)
    }

    fun endLoggingVideo(callBack: ApiCallBack<LoggingResponse>, jsonObject: JsonObject) {
        current(mContext, true).logEndVideo(jsonObject).enqueue(callBack)
    }

    fun getVimeoVideo(callBack: ApiCallBack<VimeoConfigResponse>, vimeoId: String) {
        current(mContext, false).getVimeoVideoConfig(vimeoId).enqueue(callBack)
    }

    fun getColorList(callBack: ApiCallBack<MoodColorListResponse>) {
        current(mContext, false).getColorList().enqueue(callBack)
    }

    fun getMainProgramme(callBack: ApiCallBack<MainWorkoutResponse>) {
        current(mContext, false).getMainProgramme().enqueue(callBack)
    }

    fun getAvailableMusic(callBack: ApiCallBack<AvailableMusicResponse>) {
        current(mContext, false).getAvailableMusic().enqueue(callBack)
    }

    fun getAvailableUpdate(callBack: ApiCallBack<AvailableUpdateResponse>) {
        current(mContext, false).getAvailableUpdate().enqueue(callBack)
    }


//    fun acceptItemCollector(callBack: ApiCallBack<AcceptSettlementResponse>, jsonObject: JsonObject) {
//        current(mContext, true).acceptItemCollector(jsonObject).enqueue(callBack)
//    }
}


