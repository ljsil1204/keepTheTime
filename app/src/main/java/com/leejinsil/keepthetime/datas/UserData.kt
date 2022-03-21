package com.leejinsil.keepthetime.datas

import java.util.*

class UserData(
    val id : Int,
    val provider : String,
    val uid : String?,
    val email : String,
    val ready_minute : Int,
    val nick_name : String,
    val profile_img : String,
    val created_at : Date,
    val updated_at : Date,
) {
}