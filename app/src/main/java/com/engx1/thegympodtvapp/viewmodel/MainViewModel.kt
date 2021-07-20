package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.ActiveBookingResponse
import com.engx1.thegympodtvapp.model.BookingProgrammeResponse
import com.engx1.thegympodtvapp.model.LoginResponse
import com.engx1.thegympodtvapp.model.MotivationalQuotesResponse
import com.engx1.thegympodtvapp.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val apiService: ApiService): ViewModel() {
    private val mDataMotivationalQuotes = MutableLiveData<Resource<MotivationalQuotesResponse>>()
    private val mDataGetActiveBooking = MutableLiveData<Resource<ActiveBookingResponse>>()
    private val mDataGetUserBookingData = MutableLiveData<Resource<BookingProgrammeResponse>>()
    private val mDataLogin = MutableLiveData<Resource<LoginResponse>>()

    fun getDataUserLogin(): LiveData<Resource<LoginResponse>> {
        return mDataLogin
    }

    fun login(email: String, password: String, deviceId: String, platform: String, version: String) {
        viewModelScope.launch {
            mDataLogin.postValue(Resource.loading(null))
            try {
                mDataLogin.postValue(Resource.success(data = apiService.login(email, password, deviceId, platform, version)))
            } catch (exception: Exception) {
                mDataLogin.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataUserBookingProgramme(): LiveData<Resource<BookingProgrammeResponse>> {
        return mDataGetUserBookingData
    }

    fun getUserBookingProgramme() {
        viewModelScope.launch {
            mDataGetUserBookingData.postValue(Resource.loading(null))
            try {
                mDataGetUserBookingData.postValue(Resource.success(data = apiService.getBookingProgramme(ApiService.X_ACCESS_TOKEN, "1", "home")))
            } catch (exception: Exception) {
                mDataGetUserBookingData.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataActiveBooking(): LiveData<Resource<ActiveBookingResponse>> {
        return mDataGetActiveBooking
    }

    fun getActiveBooking(time: String) {
        viewModelScope.launch {
            mDataGetActiveBooking.postValue(Resource.loading(null))
            try {
                mDataGetActiveBooking.postValue(Resource.success(data = apiService.getActiveBookings(ApiService.X_ACCESS_TOKEN, time, "1")))
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