package com.leejinsil.keepthetime.datas

import java.io.Serializable
import java.util.*

class AppointmentData(
    val id : Int,
    val user_id : Int,
    val title : String,
    val datetime : Date, // 파싱받는 데이터는 Date 형태로
    val start_place : String,
    val start_latitude : Double,
    val start_longitude : Double,
    val place : String,
    val latitude : Double,
    val longitude : Double,
    val created_at : Date,
    val user : UserData,
) : Serializable {
}