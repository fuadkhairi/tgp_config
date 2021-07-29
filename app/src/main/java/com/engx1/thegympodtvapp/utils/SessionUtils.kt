package com.engx1.thegympodtvapp.utils

import android.content.Context
import android.preference.PreferenceManager

class SessionUtils {

    companion object {
        fun saveSession(context: Context, name: String, token: String) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString("name", name)
            editor.putString("token", token)
            editor.apply()
        }

        fun getSessionName(context: Context): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString("name", "-")
        }

        fun getSessionToken(context: Context): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString("token", "")
        }

        fun removeCurrentSession(context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().remove("name").apply()
            prefs.edit().remove("token").apply()
        }
    }

}