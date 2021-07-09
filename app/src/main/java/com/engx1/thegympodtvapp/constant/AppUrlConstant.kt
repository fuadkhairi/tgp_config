package com.engx1.thegympodtvapp.constant

import com.engx1.thegympodtvapp.BuildConfig

object AppUrlConstant {
    @JvmStatic
    var BASE_URL: String = if (BuildConfig.DEBUG) {
        "https://api.staging.gympodengine.com/api/"
    } else {
        "https://api.staging.gympodengine.com/api/"
    }

}