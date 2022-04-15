package com.leejinsil.keepthetime.utils

import android.content.Context
import java.util.*

class ContextUtil {

    companion object{

        private val prefName = "keepTheTimePref"

        private val USER_TOKEN = "USER_TOKEN"
        private val AUTO_LOGIN_CHECK = "AUTO_LOGIN_CHECK"
        private val ALARM_CHECK = "ALARM_CHECK"
        private val ALARM_SPINNER_SELECTED_ITEM = "ALARM_SPINNER_SELECTED_ITEM"
        private val ALARM_RESERVATION_TIME = "ALARM_RESERVATION_TIME"
        private val ALARM_TITLE = "ALARM_TITLE"
        private val ALARM_DESCRTION = "ALARM_DESCRTION"

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

//        알람 예약 시간 저장 & 조회 (timeInMillis : Long 자료형 저장)
        fun setAlarmReservationTime (context: Context, reservationTime : Long) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putLong(ALARM_RESERVATION_TIME, reservationTime).apply()
        }

        fun getAlarmReservationTime (context: Context) : Long {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getLong(ALARM_RESERVATION_TIME, 0)
        }

//       알람 제목 저장 & 조회
        fun setAlarmTitle (context : Context, alarmTitle : String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(ALARM_TITLE, alarmTitle).apply()
        }

        fun getAlarmTitle (context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(ALARM_TITLE, "")!!
        }

//       알람 내용 저장 & 조회
        fun setAlarmDescription (context : Context, alarmDescription : String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(ALARM_DESCRTION, alarmDescription).apply()
        }

        fun getAlarmDescription (context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(ALARM_DESCRTION, "")!!
        }

    }

}