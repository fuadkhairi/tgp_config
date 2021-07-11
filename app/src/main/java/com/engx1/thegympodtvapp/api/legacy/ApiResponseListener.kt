package com.engx1.thegympodtvapp.api.legacy

interface ApiResponseListener<T> {
    fun onApiSuccess(response: T, apiName: String)
    fun onApiError(responses: String, apiName: String)
    fun onApiFailure(failureMessage: String, apiName: String)
}