package com.leejinsil.keepthetime.utils

import android.content.Context

class ContextUtil {

    companion object{

        private val prefName = "keepTheTimePref"

        private val USER_TOKEN = "USER_TOKEN"
        private val AUTO_LOGIN_CHECK = "AUTO_LOGIN_CHECK"
        private val ALARM_CHECK = "ALARM_CHECK"
        private val ALARM_SPINNER_SELECTED_ITEM = "ALARM_SPINNER_SELECTED_ITEM"

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

//        알람 체크 여부 저장 & 조회
        fun setAlarmCheck (context: Context, isAlarmCheck : Boolean) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(ALARM_CHECK, isAlarmCheck).apply()
        }

        fun getAlarmCheck (context: Context) : Boolean {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(ALARM_CHECK, false)
        }

//        알람 예약 시간 스피너 선택 아이템 저장 및 조회
        fun setAlarmSpinnerPosition (context: Context, spinnerPosition : Int) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putInt(ALARM_SPINNER_SELECTED_ITEM, spinnerPosition).apply()
        }

        fun getAlarmSpinnerPosition (context: Context) : Int {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getInt(ALARM_SPINNER_SELECTED_ITEM, 0)
        }

    }

}