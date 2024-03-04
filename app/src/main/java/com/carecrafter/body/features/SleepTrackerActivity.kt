package com.carecrafter.body.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carecrafter.R
import com.carecrafter.body.features.sleep_tracker.GoalSetSleepTrackerFragment

class SleepTrackerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_tracker)

        val fragmentA = GoalSetSleepTrackerFragment()
    }
}