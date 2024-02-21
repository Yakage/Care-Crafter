package com.carecrafter.models

import java.sql.Time

data class SleepTracker(
    var userID: String?,
    var time: Time,
    var wakeUptime: Time,
    var sleepTime: Time,
    var timeSlept: Time,
    var totalSleepTime: Time,
    var sleepScore: Int)
