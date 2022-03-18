package com.leejinsil.keepthetime.utils

import android.content.Context

class ContextUtil {

    companion object{

        private val prefName = "keepTheTimePref"

        private val USER_TOKEN = "USER_TOKEN"

        //    토큰 저장
        fun setUserToken (context : Context, token : String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(USER_TOKEN, token).apply()
        }

        //    토큰 조회
        fun getUserToken (context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(USER_TOKEN, "")!!
        }

    }

}