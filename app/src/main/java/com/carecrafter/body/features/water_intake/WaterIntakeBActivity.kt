package com.carecrafter.body.features.water_intake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.carecrafter.R
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.databinding.ActivityStatisticsWaterIntakeBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*

class WaterIntakeBActivity : AppCompatActivity() {

    private lateinit var goalEditText: EditText
    private lateinit var progressIndicator: CircularProgressIndicator
    private lateinit var totalWaterDrankTextView: TextView
    private lateinit var historyTextView: TextView
    private lateinit var middleDrinkButton: ImageButton
    private lateinit var resetButton: Button
    private lateinit var intervalSpinner: Spinner
    private lateinit var statisticButton: Button
    private lateinit var dailyGoalIndicatorTextView: TextView
    private lateinit var cupSizeSpinner: Spinner
    private lateinit var selectNotificationIntervalButton: Button
    private lateinit var notificationIntervalLayout: LinearLayout

    private var totalWaterDrank: Int = 0
    private var goalAmount: Int = 1000
    private val drinkHistory = mutableListOf<Pair<Int, String>>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_intake_bactivity)

        statisticButton = findViewById(R.id.bt_statistics)
        goalEditText = findViewById(R.id.edit_text_goal)
        cupSizeSpinner = findViewById(R.id.spinner_cup_size)
        progressIndicator = findViewById(R.id.progress_indicator)
        totalWaterDrankTextView = findViewById(R.id.text_total_water_drank)
        middleDrinkButton = findViewById(R.id.button_middle_drink)
        resetButton = findViewById(R.id.button_reset)
        intervalSpinner = findViewById(R.id.interval_spinner)
//        saveHistoryButton = findViewById(R.id.button_save_history)
        dailyGoalIndicatorTextView = findViewById(R.id.text_daily_goal_indicator)
        selectNotificationIntervalButton = findViewById(R.id.button_select_notification_interval)
        notificationIntervalLayout = findViewById(R.id.notification_interval_layout)

        sharedPreferences = this.getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        middleDrinkButton.setOnClickListener { indicateDrink(authToken.toString()) }

        resetButton.setOnClickListener { resetProgress(authToken.toString()) }
        statisticButton.setOnClickListener {
            val intent = Intent(this, StatisticsWaterIntakeActivity::class.java)
            startActivity(intent)
        }
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

        goalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    goalAmount = s.toString().toInt()
                    updateProgress(authToken.toString())
                }
            }
        })

        // Populate spinner with cup sizes and labels
        val cupSizesWithLabels = arrayOf("100 ml - Small cup", "200 ml - Medium cup", "300 ml - Large cup")
        val cupAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cupSizesWithLabels)
        cupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cupSizeSpinner.adapter = cupAdapter

        selectNotificationIntervalButton.setOnClickListener {
            if (notificationIntervalLayout.visibility == View.VISIBLE) {
                notificationIntervalLayout.visibility = View.GONE
            } else {
                notificationIntervalLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun updateProgress(authToken: String) {
        val progress = (totalWaterDrank.toFloat() / goalAmount.toFloat()) * 100
        progressIndicator.progress = progress.toInt()
        totalWaterDrankTextView.text = "Total Water Drank: $totalWaterDrank ml"

        val percentText = String.format(Locale.getDefault(), "%.1f%%", progress)
        progressIndicator.contentDescription = percentText
        createWater(authToken, totalWaterDrank.toString())

    }

    private fun indicateDrink(authToken: String) {
        val selectedCupSizeString = cupSizeSpinner.selectedItem.toString()
        val cupSize = selectedCupSizeString.split(" ")[0].toIntOrNull()

        if (cupSize != null) {
            val currentTime = Calendar.getInstance().time
            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            drinkHistory.add(Pair(cupSize, timeString))
            totalWaterDrank += cupSize
            updateProgress(authToken)
            updateHistory(authToken)

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

    private fun updateHistory(authToken: String) {
        var historyText = "Cup Size and Time:\n"
        for (drink in drinkHistory) {
            historyText += " - ${drink.first} ml at ${drink.second}\n"
        }
        createWaterHistoryData(authToken, goalAmount.toString(), totalWaterDrank.toString(), historyText)
    }

    private fun resetProgress(authToken: String) {
        totalWaterDrank = 0
        drinkHistory.clear()
        updateProgress(authToken)
        updateHistory(authToken)
        dailyGoalIndicatorTextView.visibility = View.GONE
    }

    private fun createWater(authToken: String, water: String){
        val createWaterDataJson =
            "{\"authToken\":\"$authToken\",\"water\":\"$water\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createWaterDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createWater(
                "Bearer $authToken",
                water
            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(this@WaterIntakeBActivity, t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                this@WaterIntakeBActivity,
                                "Drank Water",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Toast.makeText(
                                this@WaterIntakeBActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

    private fun createWaterHistoryData(authToken: String, daily_goal: String, current_water: String, history: String){
        val createWaterHistoryDataJson =
            "{\"authToken\":\"$authToken\",\"daily_goal\":\"$daily_goal\",\"current_water\":\"$current_water\",\"history\":\"$history\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createWaterHistoryDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createWaterHistory(
                "Bearer $authToken",
                daily_goal,
                current_water,
                history
            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(this@WaterIntakeBActivity, t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                this@WaterIntakeBActivity,
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Toast.makeText(
                                this@WaterIntakeBActivity,
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
}
