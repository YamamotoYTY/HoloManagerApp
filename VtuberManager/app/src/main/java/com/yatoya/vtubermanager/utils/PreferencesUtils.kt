package com.yatoya.vtubermanager.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtils {

    companion object {
        fun savePriorityInfo(context: Context, key: String, data: Int) {
            val dataStore: SharedPreferences =
                context.getSharedPreferences("DataStore", Context.MODE_PRIVATE)
            dataStore.edit().putInt(key, data).apply()
        }

        fun loadPriorityInfo(context: Context, key: String): Int {
            val dataStore: SharedPreferences =
                context.getSharedPreferences("DataStore", Context.MODE_PRIVATE)
            return dataStore.getInt(key, 1)
        }
    }
}