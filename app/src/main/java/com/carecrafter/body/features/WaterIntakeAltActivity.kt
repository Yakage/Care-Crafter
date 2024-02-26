package com.carecrafter.body.features

import android.os.Bundle
import android.view.View
import android.widget.*
import com.carecrafter.R
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class WaterIntakeAltActivity : AppCompatActivity() {

    private lateinit var goalEditText: EditText
    private lateinit var cupSizeEditText: EditText
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var totalWaterDrankTextView: TextView
    private lateinit var historyScrollView: ScrollView
    private lateinit var historyTextView: TextView
    private lateinit var middleDrinkButton: ImageButton
    private lateinit var resetButton: Button
    private lateinit var intervalSpinner: Spinner
    private lateinit var saveHistoryButton: Button
    private lateinit var dailyGoalIndicatorTextView: TextView

    private var totalWaterDrank: Int = 0
    private var goalAmount: Int = 1000
    private val drinkHistory = mutableListOf<Pair<Int, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_intake_alt)

        goalEditText = findViewById(R.id.edit_text_goal)
        cupSizeEditText = findViewById(R.id.edit_text_cup_size)
        progressIndicator = findViewById(R.id.progress_indicator)
        totalWaterDrankTextView = findViewById(R.id.text_total_water_drank)
        historyScrollView = findViewById(R.id.history_scroll_view)
        historyTextView = findViewById(R.id.text_history)
        middleDrinkButton = findViewById(R.id.button_middle_drink)
        resetButton = findViewById(R.id.button_reset) as Button
        intervalSpinner = findViewById(R.id.interval_spinner)
        saveHistoryButton = findViewById(R.id.button_save_history)
        dailyGoalIndicatorTextView = findViewById(R.id.text_daily_goal_indicator)

        middleDrinkButton.setOnClickListener { indicateDrink() }

        resetButton.setOnClickListener { resetProgress() }

        val intervals = arrayOf("30 minutes", "1 hour", "2 hours", "3 hours")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, intervals)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        intervalSpinner.adapter = adapter

        intervalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val interval = intervals[position]
                Toast.makeText(applicationContext, "Notification interval set to $interval", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updateProgress() {
        val progress = (totalWaterDrank.toFloat() / goalAmount.toFloat()) * 100
        progressIndicator.progress = progress.toInt()
        totalWaterDrankTextView.text = "Total Water Drank: $totalWaterDrank ml"

        val percentText = String.format(Locale.getDefault(), "%.1f%%", progress)
        progressIndicator.contentDescription = percentText
    }

    private fun indicateDrink() {
        val cupSize = cupSizeEditText.text.toString().toIntOrNull()
        if (cupSize != null) {
            val currentTime = Calendar.getInstance().time
            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            drinkHistory.add(Pair(cupSize, timeString))
            totalWaterDrank += cupSize
            updateProgress()
            updateHistory()

            if (totalWaterDrank > goalAmount) {
                showExceededGoalSnackbar()
            } else {
                dailyGoalIndicatorTextView.visibility = View.GONE
            }
        } else {
            Toast.makeText(this, "Invalid cup size input", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExceededGoalSnackbar() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "You have exceeded your daily goal!",
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }

    private fun updateHistory() {
        var historyText = "Drink History:\n"
        for (drink in drinkHistory) {
            historyText += " - ${drink.first} ml at ${drink.second}\n"
        }
        historyTextView.text = historyText
    }

    private fun resetProgress() {
        totalWaterDrank = 0
        drinkHistory.clear()
        updateProgress()
        updateHistory()
        dailyGoalIndicatorTextView.visibility = View.GONE
    }
}
