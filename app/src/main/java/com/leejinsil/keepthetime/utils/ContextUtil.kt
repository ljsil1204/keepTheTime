package com.leejinsil.keepthetime.utils

import android.content.Context

class ContextUtil {

    companion object{

        private val prefName = "keepTheTimePref"

        private val USER_TOKEN = "USER_TOKEN"
        private val AUTO_LOGIN_CHECK = "AUTO_LOGIN_CHECK"

//       토큰 저장 & 조회
        fun setUserToken (context : Context, token : String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(USER_TOKEN, token).apply()
        }

        fun getUserToken (context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(USER_TOKEN, "")!!
        }

//        자동로그인 체크박스 저장 & 조회
        fun setAutoLoginCheck (context: Context, isAutoCheck : Boolean) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN_CHECK, isAutoCheck).apply()
        }

        fun getAutoLoginCheck (context: Context) : Boolean {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN_CHECK, false)
        }

    }

}