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
        @Field("friend_list") friendList : String,
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
        @Field("start_place") startPlace : String,
        @Field("start_latitude") startLatitude : Double,
        @Field("start_longitude") startLongitude : Double,
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

    @FormUrlEncoded
    @PUT("/user/friend")
    fun putRequestAcceptOrDenyFriendRequest(
        @Field("user_id") userId : Int,
        @Field("type") type : String,
    ) : Call<BasicResponse>

    @GET("/search/user")
    fun getRequestSearchUser(
        @Query("nickname") nickname : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/friend")
    fun postRequestAddFriend(
        @Field("user_id") userId : Int,
    ) : Call<BasicResponse>

    @DELETE("/user/friend")
    fun deleteRequestFriend(
        @Query("user_id") userId : Int,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestEditPassword(
        @Field("current_password") currentPw : String,
        @Field("new_password") newPw : String,
    ) : Call<BasicResponse>

    @GET("/user/place")
    fun getRequestPlaceList() : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun postRequestAddPlace(
        @Field("name") name : String,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double,
        @Field("is_primary") is_primary : Boolean,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user/place")
    fun patchRequestEditDefaultPlace(
        @Field("place_id") placeId : Int,
    ) : Call<BasicResponse>

    @DELETE("/user/place")
    fun deleteRequestPlace(
        @Query("place_id") placeId : Int,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/password")
    fun postRequestFindPassword(
        @Field("email") email : String,
        @Field("nick_name") nickName : String,
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/appointment/arrival")
    fun postRequestAppointmentArrival(
        @Field("appointment_id") appointmentId : Int,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double,
    ) : Call<BasicResponse>

    @GET("/notifications")
    fun getRequestNotification(
        @Query("need_all_notis") needAllNotis : String
    ) : Call<BasicResponse>

    @FormUrlEncoded
    @POST("/notifications")
    fun postRequestNotificationRead(
        @Field("noti_id") notiId : Int,
    ) : Call<BasicResponse>

}