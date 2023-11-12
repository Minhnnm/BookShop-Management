package com.example.bookshopmanagement.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private const val sharedPreferencesName = "myPreferences"
    private lateinit var sharedPreferences: SharedPreferences
    const val ACCESS_TOKEN = "access_token"
    const val KEY_PERMISSION_DENIED_COUNT = "KEY_PERMISSION_DENIED_COUNT"
    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    fun putLogInTime(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLogInTime(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun putAccessToken(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, value)
        editor.apply()
    }

    fun getAccessToken(defaultValue: String?): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, defaultValue) ?: defaultValue
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        val allEntries: Map<String, *> = sharedPreferences.all
        for ((key, _) in allEntries) {
            if (key != "firstLaunch") {
                editor.remove(key)
            }
        }
        editor.apply()
    }
}