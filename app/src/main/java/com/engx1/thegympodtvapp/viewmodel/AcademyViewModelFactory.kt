package com.engx1.thegympodtvapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.engx1.thegympodtvapp.api.ApiService
import java.lang.IllegalArgumentException

class AcademyViewModelFactory(private val apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcademyViewModel::class.java)) {
            return AcademyViewModel(apiService) as T
        }
        throw IllegalArgumentException("unknown class name")
    }
}