package com.leejinsil.keepthetime.datas

import java.util.*

class NotificationData(
    val id : Int,
    val receive_user_id : Int,
    val act_user_id : Int,
    val title : String,
    val type : String,
    val message : String,
    val focus_object_id : Int,
    val is_read : Boolean,
    val created_at : Date,
    val updated_at : Date,
    val appointment : UserData?,
    val act_user : UserData,
) {
}