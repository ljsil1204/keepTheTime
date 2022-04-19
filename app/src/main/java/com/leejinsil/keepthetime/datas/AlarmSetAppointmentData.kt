package com.leejinsil.keepthetime.datas

class AlarmSetAppointmentData(
    val appointmentId : Int,
    val alarmHourSpinnerPosition : Int,
    val alarmHourSpinnerText : String,
    val alarmCheck : Boolean,
    val alarmReservationTime : Long,
    val alarmTitle : String,
    val alarmDescription : String,
) {
}