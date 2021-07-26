package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.InstructorDetailResponse
import com.engx1.thegympodtvapp.model.InstructorListResponse
import com.engx1.thegympodtvapp.model.MainWorkoutResponse
import com.engx1.thegympodtvapp.model.ProgrammeListResponse
import com.engx1.thegympodtvapp.utils.Resource
import kotlinx.coroutines.launch

class AcademyViewModel(private val apiService: ApiService): ViewModel() {
    private val mDataMainWorkout = MutableLiveData<Resource<MainWorkoutResponse>>()
    private val mDataListInstructor = MutableLiveData<Resource<InstructorListResponse>>()
    private val mDataInstructorDetail = MutableLiveData<Resource<InstructorDetailResponse>>()
    private val mDataProgrammeList = MutableLiveData<Resource<ProgrammeListResponse>>()

    fun getDataProgrammeList(): LiveData<Resource<ProgrammeListResponse>> {
        return mDataProgrammeList
    }

    fun getProgrammeList(token: String, mainProgrammeId: Int) {
        viewModelScope.launch {
            mDataProgrammeList.postValue(Resource.loading(null))
            try {
                mDataProgrammeList.postValue(Resource.success(apiService.getProgrammeList(token, mainProgrammeId)))
            } catch (exception: Exception) {
                mDataProgrammeList.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataInstructorDetail(): LiveData<Resource<InstructorDetailResponse>> {
        return mDataInstructorDetail
    }

    fun getInstructorDetail(token: String, id: Int) {
        viewModelScope.launch {
            mDataInstructorDetail.postValue(Resource.loading(null))
            try {
                mDataInstructorDetail.postValue(Resource.success(apiService.getInstructorDetail(token, id)))
            } catch (exception: Exception) {
                mDataInstructorDetail.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }


    fun getDataListInstructor(): LiveData<Resource<InstructorListResponse>> {
        return mDataListInstructor
    }

    fun getListInstructor(token: String, limit: Int) {
        viewModelScope.launch {
            mDataListInstructor.postValue(Resource.loading(null))
            try {
                mDataListInstructor.postValue(Resource.success(apiService.getInstructor(token, limit)))
            } catch (exception: Exception) {
                mDataListInstructor.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }

    fun getDataMainWorkout(): LiveData<Resource<MainWorkoutResponse>> {
        return mDataMainWorkout
    }

    fun getMainWorkout(token: String) {
        viewModelScope.launch {
            mDataMainWorkout.postValue(Resource.loading(null))
            try {
                mDataMainWorkout.postValue(Resource.success(apiService.getAcademyMainProgramme(token)))
            } catch (exception: Exception) {
                mDataMainWorkout.postValue(Resource.error(exception.message.toString(), data = null))
            }
        }
    }
}