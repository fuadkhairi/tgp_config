package com.engx1.thegympodtvapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.engx1.thegympodtvapp.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class SharedPrefManager(var context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun removeFromPreference(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    companion object {
        fun savePreferenceString(context: Context?, key: String?, value: String?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getPreferenceString(context: Context?, key: String?): String? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(key, "")
        }

        fun savePreferenceInt(context: Context?, key: String?, value: Int) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getPreferenceInt(context: Context?, key: String?): Int? {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getInt(key, 0)
        }

        fun savePreferenceBoolean(context: Context?, key: String?, b: Boolean) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, b)
            editor.apply()
        }

        fun getBooleanPreferences(context: Context?, key: String?): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(key, false)
        }

        /**
         * Removes all the fields from SharedPrefs
         */
        fun clearPrefs(context: Context?) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        fun saveMapPref(context: Context?, key: String?, inputMap: MutableMap<Int, Int>) {
            val intToString = HashMap<String, String>()
            for ((key1, value) in inputMap) {
                intToString[key1.toString()] = value.toString()
            }
            val gson = Gson()
            val hashToString = gson.toJson(intToString)
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, hashToString).apply()
        }

        fun loadMapPref(context: Context?, key: String?): HashMap<Int, Int> {
            val stringToInt = HashMap<Int, Int>()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val storedMapString = sharedPreferences.getString(key, "")
            val type = object : TypeToken<HashMap<String?, String?>?>() {}.type
            val gson = Gson()
            val stringHashMap = gson.fromJson<HashMap<String, String>>(storedMapString, type)
            if (stringHashMap != null) {
                for ((key1, value) in stringHashMap) {
                    stringToInt[Integer.valueOf(key1)] = Integer.valueOf(value)
                }
            }
            return stringToInt
        }

        fun saveHashMap(context: Context?, key: String?, obj: Any?) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(obj)
            editor.putString(key, json)
            editor.apply()
        }

        fun removeHashMap(context: Context, key: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit().remove(key).apply()
        }
    }

}