package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.engx1.thegympodtvapp.api.ApiService
import com.engx1.thegympodtvapp.model.MainWorkoutResponse
import com.engx1.thegympodtvapp.utils.Resource

class WorkoutViewModel(private val apiService: ApiService): ViewModel() {
    private val mDataMainWorkoutProgramme = MutableLiveData<Resource<MainWorkoutResponse>>()
}