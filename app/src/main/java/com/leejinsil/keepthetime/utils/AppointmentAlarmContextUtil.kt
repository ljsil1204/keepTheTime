package com.leejinsil.keepthetime.utils

import android.content.Context

class AppointmentAlarmContextUtil{

    companion object{

        private fun makePrefName(appointmentId: Int) : String{
            return "appoinmentAlamPref_${appointmentId}"
        }

        private val APPOINTMENT_ID = "APPOINTMENT_ID"
        private val ALARM_CHECK = "ALARM_CHECK"
        private val ALARM_SPINNER_SELECTED_ITEM_POSITION = "ALARM_SPINNER_SELECTED_ITEM_POSITION"
        private val ALARM_SPINNER_SELECTED_ITEM_TEXT = "ALARM_SPINNER_SELECTED_ITEM_TEXT"
        private val ALARM_RESERVATION_TIME = "ALARM_RESERVATION_TIME"
        private val ALARM_TITLE = "ALARM_TITLE"
        private val ALARM_DESCRTION = "ALARM_DESCRTION"


//        약속 id 저장 & 조회
        fun setAppointmentId (context: Context, appointmentId : Int) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putInt(APPOINTMENT_ID, appointmentId).apply()
        }

        fun getAppointmentId (context: Context, appointmentId: Int) : Int {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getInt(APPOINTMENT_ID, 0)
        }

//        알람 체크 여부 저장 & 조회
        fun setAlarmCheck (context: Context,appointmentId: Int, isAlarmCheck : Boolean) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putBoolean(ALARM_CHECK, isAlarmCheck).apply()
        }

        fun getAlarmCheck (context: Context, appointmentId: Int) : Boolean {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getBoolean(ALARM_CHECK, false)
        }

//        알람 예약 시간 스피너 선택 아이템 포지션 저장 및 조회
        fun setAlarmSpinnerPosition (context: Context, appointmentId: Int, spinnerPosition : Int) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putInt(ALARM_SPINNER_SELECTED_ITEM_POSITION, spinnerPosition).apply()
        }

        fun getAlarmSpinnerPosition (context: Context, appointmentId: Int) : Int {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getInt(ALARM_SPINNER_SELECTED_ITEM_POSITION, 0)
        }

//        알람 예약 시간 스피너 아이템 텍스트 저장 및 조회
        fun setAlarmSpinnerText (context : Context, appointmentId: Int, text : String) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putString(ALARM_SPINNER_SELECTED_ITEM_TEXT, text).apply()
        }

        fun getAlarmSpinnerText (context: Context, appointmentId: Int) : String {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getString(ALARM_SPINNER_SELECTED_ITEM_TEXT, "약속 정시")!!
        }

//        알람 예약 시간 저장 & 조회 (timeInMillis : Long 자료형 저장)
        fun setAlarmReservationTime (context: Context, appointmentId: Int, reservationTime : Long) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putLong(ALARM_RESERVATION_TIME, reservationTime).apply()
        }

        fun getAlarmReservationTime (context: Context, appointmentId: Int) : Long {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getLong(ALARM_RESERVATION_TIME, 0)
        }

//       알람 제목 저장 & 조회
        fun setAlarmTitle (context : Context, appointmentId: Int, alarmTitle : String) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putString(ALARM_TITLE, alarmTitle).apply()
        }

        fun getAlarmTitle (context: Context, appointmentId: Int) : String {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getString(ALARM_TITLE, "")!!
        }

//       알람 내용 저장 & 조회
        fun setAlarmDescription (context : Context, appointmentId: Int, alarmDescription : String) {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            pref.edit().putString(ALARM_DESCRTION, alarmDescription).apply()
        }

        fun getAlarmDescription (context: Context, appointmentId: Int) : String {
            val pref = context.getSharedPreferences(makePrefName(appointmentId), Context.MODE_PRIVATE)
            return pref.getString(ALARM_DESCRTION, "")!!
        }

    }

}