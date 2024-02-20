package com.carecrafter.retrofit_database

import com.carecrafter.models.DefaultResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface CareCrafterInterfaces {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("register")
    fun createUser(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("age") age:String,
        @Field("height") height:String,
        @Field("weight") weight:String,
        @Field("gender") gender:String,
        @Field("password") password:String,
        @Field("confirm_password") confirmPasswordData:String,
    ):retrofit2.Call<DefaultResponse>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login")
    fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String,
    ):retrofit2.Call<DefaultResponse>
}