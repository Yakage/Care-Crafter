package com.carecrafter.models

data class StepTracker(
    val id: Int,
    val currentStepsPerDay: Int,
    val totalStepsTaken: Int,
    val averageStepTaken: Int,
    val dailyGoalStepPerMonthOrYear: Int,
)
