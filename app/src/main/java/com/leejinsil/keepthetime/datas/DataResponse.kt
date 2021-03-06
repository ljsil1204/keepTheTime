package com.leejinsil.keepthetime.datas

class DataResponse(
    val user : UserData,
    val token : String,
    val appointments : List<AppointmentData>,
    val invited_appointments : List<AppointmentData>,
    val appointment : AppointmentData,
    val friends : List<UserData>,
    val users : List<UserData>,
    val places : List<PlaceData>,
    val notifications : List<NotificationData>,
    var unread_noty_count : Int,
) {
}