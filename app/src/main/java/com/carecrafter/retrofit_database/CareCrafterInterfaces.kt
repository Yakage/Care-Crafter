package com.carecrafter.retrofit_database

import com.carecrafter.models.Alarm
import com.carecrafter.models.BMI
import com.carecrafter.models.StepsApi
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.User
import com.carecrafter.models.WaterApi
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface CareCrafterInterfaces {
    // For User
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

    //For Sleep Tracker
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

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createSleep")
    fun createSleep(
        @Header("Authorization") authToken: String,
        @Field("steps") results:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getDailySleep")
    fun getDailySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    @Headers("Accept: application/json")
    @GET("getWeeklySleep")
    fun getWeeklySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    @Headers("Accept: application/json")
    @GET("getMonthlySleep")
    fun getMonthlySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    //For BMI
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createBMI")
    fun createBMI(
        @Header("Authorization") authToken: String,
        @Field("results") results:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getBMI")
    fun getBMI(@Header("Authorization") authToken: String): Call<BMI>

    //For Step
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createStepHistory")
    fun createStepHistory(
        @Header("Authorization") authToken: String,
        @Field("step_history") results:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createStep")
    fun createStep(
        @Header("Authorization") authToken: String,
        @Field("steps") results:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getDailyStep")
    fun getDailySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>

    @Headers("Accept: application/json")
    @GET("getWeeklyStep")
    fun getWeeklySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>

    @Headers("Accept: application/json")
    @GET("getMonthlyStep")
    fun getMonthlySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>

    //For Water Intake
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createWater")
    fun createWater(
        @Header("Authorization") authToken: String,
        @Field("water") results:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getDailyWater")
    fun getDailyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>

    @Headers("Accept: application/json")
    @GET("getWeeklyWater")
    fun getWeeklyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>

    @Headers("Accept: application/json")
    @GET("getMonthlyWater")
    fun getMonthlyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>
}