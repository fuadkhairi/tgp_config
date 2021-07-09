package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.ActiveBookingResponse
import com.engx1.thegympodtvapp.model.MotivationalQuotesResponse
import com.engx1.thegympodtvapp.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val apiService: ApiService): ViewModel() {
    private val mDataMotivationalQuotes = MutableLiveData<Resource<MotivationalQuotesResponse>>()
    private val mDataGetActiveBooking = MutableLiveData<Resource<ActiveBookingResponse>>()

    fun getDataActiveBooking(): LiveData<Resource<ActiveBookingResponse>> {
        return mDataGetActiveBooking
    }

    fun getActiveBooking() {
        viewModelScope.launch {
            mDataGetActiveBooking.postValue(Resource.loading(null))
            try {
                mDataGetActiveBooking.postValue(Resource.success(data = apiService.getActiveBookings(ApiService.X_ACCESS_TOKEN, "20:00", "26")))
            } catch (exception: Exception) {
                mDataGetActiveBooking.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataMotivationalQuotes(): LiveData<Resource<MotivationalQuotesResponse>> {
        return mDataMotivationalQuotes
    }

    fun getMotivationalQuotes() {
        viewModelScope.launch {
            mDataMotivationalQuotes.postValue(Resource.loading(null))
            try {
                mDataMotivationalQuotes.postValue(Resource.success(data = apiService.getMotivationQuotes(ApiService.X_ACCESS_TOKEN)))
            } catch (exception: Exception) {
                mDataMotivationalQuotes.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }
}