package com.taskmanager.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Acesso a dados rápidos do projeto - SharedPreferences
 */
class LocalTaskData(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("locataskdata", Context.MODE_PRIVATE)

    fun save(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun saveID(key: String, value : Long){
        preferences.edit().putLong(key, value).apply()
    }
    fun delete(key: String) {
        preferences.edit().remove(key).apply()
    }
    fun get(key: String): String {
        return preferences.getString(key, "") ?: ""
    }

    fun getByID(key: String): Long{
        return  preferences.getLong(key, 0L)
    }
}