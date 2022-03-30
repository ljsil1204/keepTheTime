package com.leejinsil.keepthetime.api

import com.leejinsil.keepthetime.datas.AppointmentData
import com.leejinsil.keepthetime.datas.BasicResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email") email : String,
        @Field("password") password : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("nick_name") nickname : String,
    ) : Call<BasicResponse>

    @GET("/user/check")
    fun getRequestDuplicatedCheck(
        @Query("type") type : String,
        @Query("value") value: String,
    ) : Call<BasicResponse>

    @GET("/user")
    fun getRequestMyInfo() : Call<BasicResponse>

    @GET("/appointment")
    fun getRequestMyAppointmentList() : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/appointment")
    fun postRequestAddAppointment(
        @Field("title") title : String,
        @Field("datetime") datetime : String,

        @Field("start_place") start_place : String,
        @Field("start_latitude") startlat : Double,
        @Field("start_longitude") startlng : Double,

        @Field("place") place : String,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double,
    ) : Call<BasicResponse>

    @GET("/appointment/{appointment_id}")
    fun getRequestMyAppointmentDetail(
        @Path("appointment_id") appointmentId : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PUT("/appointment")
    fun putRequestEditAppointment(
        @Field("appointment_id") appointment_id : Int,
        @Field("title") title : String,
        @Field("datetime") datetime : String,
        @Field("place") place : String,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double,
    ) : Call<BasicResponse>

    @DELETE("/appointment")
    fun deleteRequestAppointment(
        @Query("appointment_id") appointment_id : Int,
    ) : Call<BasicResponse>

    @Multipart
    @PUT("/user/image")
    fun putRequestProfileImg(
        @Part img: MultipartBody.Part
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestUserInfoEdit(
        @Field("field") field : String,
        @Field("value") value : String,
    ) : Call<BasicResponse>

    @DELETE("/user/image")
    fun deleteRequestProfileImage() : Call<BasicResponse>

    @DELETE("/user")
    fun deleteRequestUser(
        @Query("text") text : String,
    ) : Call<BasicResponse>

    @GET("/user/friend")
    fun getrequestFriendList(
        @Query("type") type : String, // all my, requested
    ) : Call<BasicResponse>

}