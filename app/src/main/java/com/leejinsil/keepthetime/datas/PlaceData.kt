package com.leejinsil.keepthetime.datas

import com.google.gson.annotations.SerializedName

class PlaceData(
    val id : Int,
    val userId : Int,
    val name : String,
    val latitude : Double,
    val longitude : Double,
    @SerializedName("is_primary") val isPrimary : Boolean,
) {
}