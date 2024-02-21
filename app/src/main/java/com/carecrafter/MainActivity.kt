package com.carecrafter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.carecrafter.roomdatabase.AppDatabase


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var database: AppDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-database"
        ).build()
    }
}