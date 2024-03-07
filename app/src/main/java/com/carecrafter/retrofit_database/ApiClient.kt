package com.carecrafter.retrofit_database
import android.content.SharedPreferences
import android.util.Base64
import android.webkit.CookieSyncManager.createInstance
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
object ApiClient {
    private const val BASE_URL = "https://carecrafter-e36f7bd1d791.herokuapp.com/api/"

    private lateinit var sharedPreferences: SharedPreferences

    // Initialize the Retrofit instance lazily
    private val retrofitInstance: CareCrafterInterfaces by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(CareCrafterInterfaces::class.java)
    }

    // Set SharedPreferences before using the Retrofit instance
    fun setSharedPreferences(pref: SharedPreferences) {
        sharedPreferences = pref
    }

    private fun getAuthToken(): String {
        if (!::sharedPreferences.isInitialized) {
            throw IllegalStateException("SharedPreferences has not been initialized. Call setSharedPreferences() before using PHINMAClient.")
        }
        val authToken = sharedPreferences.getString("authToken", "")
        return "Bearer $authToken"
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", getAuthToken())
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Add logging interceptor
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Expose the Retrofit instance
    val instance: CareCrafterInterfaces
        get() = retrofitInstance

}