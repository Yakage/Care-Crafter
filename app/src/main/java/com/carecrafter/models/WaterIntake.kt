package com.carecrafter.models

data class WaterIntake(
    val id: Int,
    val currentStepsPerDay: Int,
    val totalStepsTaken: Int,
    val averageStepTaken: Int,
    val dailyGoalStepPerMonthOrYear: Int,
)
