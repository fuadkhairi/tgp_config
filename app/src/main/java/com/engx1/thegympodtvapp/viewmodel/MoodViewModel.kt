package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.LightStateResponse
import com.engx1.thegympodtvapp.model.MoodColorListResponse
import com.engx1.thegympodtvapp.model.MoodStateResponse
import com.engx1.thegympodtvapp.model.MotivationalQuotesResponse
import com.engx1.thegympodtvapp.utils.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class MoodViewModel(private val apiService: ApiService): ViewModel() {
    private val mDataSetLightState = MutableLiveData<Resource<LightStateResponse>>()
    private val mDataSetMoodState = MutableLiveData<Resource<MoodStateResponse>>()
    private val mDataMoodColorList = MutableLiveData<Resource<MoodColorListResponse>>()

    fun getDataSetLightState(): LiveData<Resource<LightStateResponse>> {
        return mDataSetLightState
    }

    fun setLightState(newState: Boolean, gympodId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("gympod_id", gympodId)
        jsonObject.addProperty("control_status", newState)
        viewModelScope.launch {
            mDataSetLightState.postValue(Resource.loading(null))
            try {
                mDataSetLightState.postValue(Resource.success(apiService.setLightState(ApiService.X_ACCESS_TOKEN, jsonObject)))
            } catch (exception: Exception) {
                mDataSetLightState.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataSetMoodState(): LiveData<Resource<MoodStateResponse>> {
        return mDataSetMoodState
    }

    fun setMoodState(newState: Boolean, gympodId: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("gympod_id", gympodId)
        jsonObject.addProperty("control_status", newState)
        viewModelScope.launch {
            mDataSetMoodState.postValue(Resource.loading(null))
            try {
                mDataSetMoodState.postValue(Resource.success(apiService.setMoodState(ApiService.X_ACCESS_TOKEN, jsonObject)))
            } catch (exception: Exception) {
                mDataSetMoodState.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataMoodColorList(): LiveData<Resource<MoodColorListResponse>> {
        return mDataMoodColorList
    }

    fun getMoodColorList() {
        viewModelScope.launch {
            mDataMoodColorList.postValue(Resource.loading(null))
            try {
                mDataMoodColorList.postValue(Resource.success(apiService.getColorList(ApiService.X_ACCESS_TOKEN)))
            } catch (exception: Exception) {
                mDataMoodColorList.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }
}