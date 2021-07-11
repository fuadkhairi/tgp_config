package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.api.ApiService
import java.lang.IllegalArgumentException

class WorkoutViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(apiService) as T
        }
        throw IllegalArgumentException("unknown class name")
    }
}