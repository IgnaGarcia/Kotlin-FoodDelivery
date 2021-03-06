package com.example.ejercicio3.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.ejercicio3.data.entities.User
import com.google.gson.Gson

object SharedPreferencesManager{
    private const val PREFERENCES_NAME = "MyPreferences"
    private const val USER_KEY = "user"
    private val gson = Gson()

    private fun getPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveUser(context: Context, user: User) {
        getPreferences(context)
                .edit()
                .putString(USER_KEY, gson.toJson(user))
                .apply()
    }

    fun getUser(context: Context): User? {
        return gson.fromJson(getPreferences(context)
            .getString(USER_KEY, ""), User::class.java)
    }

    fun clearData(context: Context) {
        getPreferences(context).edit().clear().apply()
    }
}