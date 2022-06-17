package com.example.storyapp.model

import android.content.Context

internal class SystemPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: UserModel){
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN_KEY, value.token)
        editor.putBoolean(STATE_KEY, value.isLogin)
        editor.apply()
    }

    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "")
        model.userId = preferences.getString(USER_ID, "")
        model.token = preferences.getString(TOKEN_KEY, "")
        model.isLogin = preferences.getBoolean(STATE_KEY, false)
        return model
    }

    companion object{
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val USER_ID = "userId"
        private const val TOKEN_KEY = "token"
        private const val STATE_KEY = "isLogin"
    }
}