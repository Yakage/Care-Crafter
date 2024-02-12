package com.carecrafter.sqlitedatabase

data class StepTracker(
    var userID: Int,
    var currentStepsPerDay: Int,
    var totalStepsTaken: Int,
    var averageStepTaken: Int,
    var dailyGoalStepPerMonthOrYear: Int)
