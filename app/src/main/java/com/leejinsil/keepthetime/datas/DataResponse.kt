package com.leejinsil.keepthetime.datas

class DataResponse(
    val user : UserData,
    val token : String,
    val appointments : List<AppointmentData>,
    val appointment : AppointmentData,
) {
}