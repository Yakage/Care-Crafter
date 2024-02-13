package com.carecrafter.sqlitedatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
                "UserID INTEGER," +
                "Time TEXT," +
                "WakeUpTime TEXT," +
                "SleepTime TEXT," +
                "TimeSlept TEXT," +
                "TotalSleepTime TEXT," +
                "SleepScore INTEGER," +
                "FOREIGN KEY(UserID) REFERENCES User(UserID))")

        val CREATE_WATER_INTAKE_TABLE = ("CREATE TABLE WaterIntake (" +
                "UserID INTEGER," +
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
                "UserID INTEGER," +
                "CurrentStepsPerDay INTEGER," +
                "TotalStepsTaken INTEGER," +
                "AverageStepTaken INTEGER," +
                "DailyGoalStepPerMonthOrYear INTEGER," +
                "FOREIGN KEY(UserID) REFERENCES User(UserID))")

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
}