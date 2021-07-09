package com.engx1.thegympodtvapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object CommonUtils {
    fun isOnline(mContext: Context): Boolean {
        val connectivity = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkInfo = connectivity.allNetworkInfo
        for (i in netWorkInfo.indices)
            if (netWorkInfo[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }
}