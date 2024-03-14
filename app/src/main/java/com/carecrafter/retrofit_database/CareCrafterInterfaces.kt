package com.carecrafter.retrofit_database

import com.carecrafter.models.Alarm
import com.carecrafter.models.BMI
import com.carecrafter.models.StepsApi
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.SleepHistoryApi
import com.carecrafter.models.SleepScoreLogs
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.StepHistoryApi
import com.carecrafter.models.StepsDailyStatsApi
import com.carecrafter.models.StepsMonthlyStatsApi
import com.carecrafter.models.StepsWeeklyStatsApi
import com.carecrafter.models.User
import com.carecrafter.models.WaterApi
import com.carecrafter.models.WaterDailyStatsApi
import com.carecrafter.models.WaterHistoryApi
import com.carecrafter.models.WaterMonthlyStatsApi
import com.carecrafter.models.WaterWeeklyStatsApi
import com.google.gson.annotations.SerializedName
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
    @POST("logout")
    fun logoutUser(
        @Header("Authorization") authToken: String,
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
        @Field("avatar") avatar:Int,
    ): retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("users")
    fun getUser(@Header("Authorization") authToken: String): Call<User>

    //For Sleep Tracker =======================================================================
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createAlarm")
    fun createAlarm(
        @Header("Authorization") authToken: String,
        @Field("daily_goal") dailyGoal: String,
        @Field("time") time: String,
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
        @Field("total_time") totalTime:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getScore")
    fun getScore(@Header("Authorization") authToken: String): Call<SleepScoreLogs>

    @Headers("Accept: application/json")
    @GET("getSleepTime")
    fun getSleepTime(@Header("Authorization") authToken: String): Call<SleepsApi>

    @Headers("Accept: application/json")
    @GET("getSleepHistory")
    fun getSleepHistory(@Header("Authorization") authToken: String): Call<List<SleepHistoryApi>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createSleep")
    fun createSleep(
        @Header("Authorization") authToken: String,
        @Field("score") score:String,
        @Field("sleeps") sleeps:Int,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("totalSleeps")
    fun totalSleeps(@Header("Authorization") authToken: String): Call<SleepsApi>
    @Headers("Accept: application/json")
    @GET("showDailySleep")
    fun showDailySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    @Headers("Accept: application/json")
    @GET("showWeeklySleep")
    fun showWeeklySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    @Headers("Accept: application/json")
    @GET("showMonthlySleep")
    fun showMonthlySleep(@Header("Authorization") authToken: String): Call<List<SleepsApi>>

    //For BMI ===================================================================================
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createBMI")
    fun createBMI(
        @Header("Authorization") authToken: String,
        @Field("bmi") bmi:String,
        @Field("category") category:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("getBMI")
    fun getBMI(@Header("Authorization") authToken: String): Call<BMI>

    //For Step Tracker =========================================================================
    @Headers("Accept: application/json")
    @GET("getStepHistory")
    fun getStepHistory(@Header("Authorization") authToken: String): Call<StepHistoryApi>

    @Headers("Accept: application/json")
    @GET("getStepHistory2")
    fun getStepHistory2(@Header("Authorization") authToken: String): Call<List<StepHistoryApi>>
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createStepHistory")
    fun createStepHistory(
        @Header("Authorization") authToken: String,
        @Field("daily_goal") dailyGoal:String,
        @Field("current_steps") currentSteps:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @POST("updateStepHistory")
    fun updateStepHistory(
        @Header("Authorization") authToken: String,
        @Field("current_steps") currentSteps:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createStep")
    fun createStep(
        @Header("Authorization") authToken: String,
        @Field("steps") results:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @PUT("updateStep")
    fun updateStep(
        @Header("Authorization") authToken: String,
        @Field("steps") results:Int,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("totalSteps")
    fun totalSteps(@Header("Authorization") authToken: String): Call<StepsApi>

    //For Leaderboards
    @Headers("Accept: application/json")
    @GET("showDailyStep")
    fun showDailySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>


    @Headers("Accept: application/json")
    @GET("showWeeklyStep")
    fun showWeeklySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>

    @Headers("Accept: application/json")
    @GET("showMonthlyStep")
    fun showMonthlySteps(@Header("Authorization") authToken: String): Call<List<StepsApi>>

    //For Statistics
    @Headers("Accept: application/json")
    @GET("chartDataStepsWeekly")
    fun chartDataStepsWeekly(@Header("Authorization") authToken: String): Call<List<StepsWeeklyStatsApi>>

    @Headers("Accept: application/json")
    @GET("chartDataStepsMonthly")
    fun chartDataStepsMonthly(@Header("Authorization") authToken: String): Call<List<StepsMonthlyStatsApi>>

    //For Water Intake ============================================================================
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createWater")
    fun createWater(
        @Header("Authorization") authToken: String,
        @Field("water") water:String,
    ):retrofit2.Call<DefaultResponse>

    @Headers("Accept: application/json")
    @GET("totalWater")
    fun totalWater(@Header("Authorization") authToken: String): Call<WaterApi>

    // For Leaderboards
    @Headers("Accept: application/json")
    @GET("showDailyWater")
    fun showDailyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>

    @Headers("Accept: application/json")
    @GET("showWeeklyWater")
    fun showWeeklyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>

    @Headers("Accept: application/json")
    @GET("showMonthlyWater")
    fun showMonthlyWater(@Header("Authorization") authToken: String): Call<List<WaterApi>>

    @Headers("Accept: application/json")
    @GET("getWaterHistory")
    fun getWaterHistory(@Header("Authorization") authToken: String): Call<WaterHistoryApi>

    @Headers("Accept: application/json")
    @GET("getWaterHistory2")
    fun getWaterHistory2(@Header("Authorization") authToken: String): Call<List<WaterHistoryApi>>
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("createWaterHistory")
    fun createWaterHistory(
        @Header("Authorization") authToken: String,
        @Field("daily_goal") dailyGoal:String,
        @Field("current_water") currentWater:String,
        @Field("history") history:String,
    ):retrofit2.Call<DefaultResponse>

    //For Statistics
    @Headers("Accept: application/json")
    @GET("chartDataWaterWeekly")
    fun chartDataWaterWeekly(@Header("Authorization") authToken: String): Call<List<WaterWeeklyStatsApi>>

    @Headers("Accept: application/json")
    @GET("chartDataWaterMonthly")
    fun chartDataWaterMonthly(@Header("Authorization") authToken: String): Call<List<WaterMonthlyStatsApi>>
}