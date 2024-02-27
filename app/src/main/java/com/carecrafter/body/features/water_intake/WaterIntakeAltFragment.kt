package com.carecrafter.body.features.water_intake

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.carecrafter.R
import java.text.SimpleDateFormat
import java.util.*

class WaterIntakeAltFragment : Fragment(R.layout.fragment_water_intake_alt) {

    private lateinit var goalEditText: EditText
    private lateinit var cupSizeEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var totalWaterDrankTextView: TextView
    private lateinit var historyTextView: TextView
    private lateinit var drinkButton: Button
    private lateinit var middleDrinkButton: Button

    private var totalWaterDrank: Int = 0
    private var goalAmount: Int = 2000 // Default goal
    private val drinkHistory = mutableListOf<Pair<Int, String>>() // Pair of drank amount and time

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        goalEditText = view.findViewById(R.id.edit_text_goal)
        cupSizeEditText = view.findViewById(R.id.edit_text_cup_size)
        timeEditText = view.findViewById(R.id.edit_text_time)
        progressBar = view.findViewById(R.id.progress_bar)
        totalWaterDrankTextView = view.findViewById(R.id.text_total_water_drank)
        historyTextView = view.findViewById(R.id.text_history)
        drinkButton = view.findViewById(R.id.button_drink)
        middleDrinkButton = view.findViewById(R.id.button_middle_drink)

        // Set up button click listeners
        drinkButton.setOnClickListener { indicateDrink() }
        middleDrinkButton.setOnClickListener { indicateDrink() }

        // Set initial progress
        updateProgress()
    }

    private fun updateProgress() {
        val progress = (totalWaterDrank.toFloat() / goalAmount.toFloat()) * 100
        progressBar.progress = progress.toInt()
        totalWaterDrankTextView.text = "Total Water Drank: $totalWaterDrank ml"
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
        } else {
            // Handle invalid cup size input
        }
    }

    private fun updateHistory() {
        var historyText = "Drink History:\n"
        for (drink in drinkHistory) {
            historyText += " - ${drink.first} ml at ${drink.second}\n"
        }
        historyTextView.text = historyText
    }
}
