package com.carecrafter.sqlitedatabase

import java.sql.Time

data class WaterIntake(
    var userID: Int,
    var dailyGoal: Int,
    var totalWaterIntakeForTheDay: Int,
    var currentWaterIntakeForTheDay: Int,
    var reminder: Time,
    var todayLog: String?,
    var reminderInterval: Int,
    var averageVolume: Int,
    var averageCompletion: Int,
    var drinkFrequency: Int)
