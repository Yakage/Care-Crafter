package com.carecrafter.sqlitedatabase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.carecrafter.models.User

class CareCrafterDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CareCrafter.db"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = ("CREATE TABLE User (" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT," +
                "Email TEXT," +
                "Age INTEGER," +
                "Height REAL," +
                "Gender TEXT," +
                "Weight REAL," +
                "Password TEXT," +
                "ConfirmPassword TEXT," +
                "Role TEXT," +
                "Status TEXT)")

        val CREATE_SLEEP_TRACKER_TABLE = ("CREATE TABLE SleepTracker (" +
                "ID INTEGER INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Time TEXT," +
                "WakeUpTime TEXT," +
                "SleepTime TEXT," +
                "TimeSlept TEXT," +
                "TotalSleepTime TEXT," +
                "SleepScore INTEGER," +
                "FOREIGN KEY(UserID) REFERENCES User(UserID))")

        val CREATE_WATER_INTAKE_TABLE = ("CREATE TABLE WaterIntake (" +
                "ID INTEGER INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DailyGoal INTEGER," +
                "TotalWaterIntakeForTheDay REAL," +
                "CurrentWaterIntakeForTheDay REAL," +
                "Reminder TEXT," +
                "TodayLog TEXT," +
                "ReminderInterval INTEGER," +
                "AverageVolume REAL," +
                "AverageCompletion REAL," +
                "DrinkFrequency INTEGER," +
                "FOREIGN KEY(UserID) REFERENCES User(UserID))")

        val CREATE_STEP_TRACKER_TABLE = ("CREATE TABLE StepTracker (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CurrentStepsPerDay INTEGER," +
                "TotalStepsTaken INTEGER," +
                "AverageStepTaken INTEGER," +
                "DailyGoalStepPerMonthOrYear INTEGER,") //+
                //"FOREIGN KEY(UserID) REFERENCES User(UserID))")

        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_SLEEP_TRACKER_TABLE)
        db.execSQL(CREATE_WATER_INTAKE_TABLE)
        db.execSQL(CREATE_STEP_TRACKER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS User")
        db.execSQL("DROP TABLE IF EXISTS SleepTracker")
        db.execSQL("DROP TABLE IF EXISTS WaterIntake")
        db.execSQL("DROP TABLE IF EXISTS StepTracker")
        onCreate(db)
    }


    //functions for User
    fun insertUser(user: User){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Name", user.name)
            put("Email", user.email)
            put("Age", user.age)
            put("Height", user.height)
            put("Gender", user.gender)
            put("Weight", user.weight)
            put("Password", user.password)
            put("ConfirmPassword", user.confirmPassword)
            put("Role", user.role)
            put("Status", user.status)
        }

        db.insert("User", null, values)
        db.close()
    }

    //functions for WaterIntake
}