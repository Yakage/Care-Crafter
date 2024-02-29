package com.carecrafter.retrofit_database

import android.service.autofill.UserData
import com.carecrafter.models.Alarm
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CareCrafterInterfaces {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun createUser(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("birthday") birthday:String,
        @Field("height") height:String,
        @Field("weight") weight:String,
        @Field("gender") gender:String,
        @Field("password") password:String,
        @Field("confirm_password") confirmPassword:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @PUT("updateUser")
    fun updateUser(
        @Header("Authorization") authToken: String,
        @Field("name") name:String,
        @Field("birthday") birthday:String,
        @Field("gender") gender:String,
        @Field("height") height:String,
        @Field("weight") weight:String,
    ): retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("users")
    fun getUser(@Header("Authorization") authToken: String): Call<User>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createAlarm")
    fun createAlarm(
        @Header("Authorization") authToken: String,
        @Field("title") title: String,
        @Field("message") message: String,
        @Field("time") time: String,
        @Field("date") date: String
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getAlarm")
    fun getAlarm(@Header("Authorization") authToken: String): Call<Alarm>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createScore")
    fun createScore(
        @Header("Authorization") authToken: String,
        @Field("score_logs") score:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getScore")
    fun getScore(@Header("Authorization") authToken: String): Call<User>
}