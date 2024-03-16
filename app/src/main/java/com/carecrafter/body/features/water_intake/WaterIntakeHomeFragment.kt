package com.carecrafter.body.features.water_intake

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.WaterIntakeHomeBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.WaterHistoryApi
import com.carecrafter.retrofit_database.ApiClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WaterIntakeHomeFragment : Fragment() {

    private lateinit var binding: WaterIntakeHomeBinding

    private val channelId = "notification_channel"
    private var notificationId = 0
    private val handler = Handler(Looper.getMainLooper())

    private var totalWaterDrank: Int = 0
    private var goalAmount: Int = 1000
    private val drinkHistory = mutableListOf<Pair<Int, String>>()

    private lateinit var sharedPreferences: SharedPreferences

    var current_water = 0
    var progress = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WaterIntakeHomeBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getWaterHistory(it) }

        createNotificationChannel()

        binding.buttonMiddleDrink.setOnClickListener {
            indicateDrink(authToken.toString())
            binding.editTextGoal.visibility = View.GONE
            handler.postDelayed({ sendNotification() }, 1*1000)
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigate(WaterIntakeHomeFragmentDirections.actionWaterIntakeHomeFragmentToHomeFragment())
        }
        binding.buttonReset.setOnClickListener {
            resetProgress(authToken.toString())
            binding.editTextGoal.visibility = View.VISIBLE
            binding.cupCapacity.visibility = View.VISIBLE
        }
        binding.btStatistics.setOnClickListener {
            findNavController().navigate(WaterIntakeHomeFragmentDirections.actionWaterIntakeHomeFragmentToStatisticFragment2())
        }

        val intervals = arrayOf("30 minutes", "1 hour", "2 hours", "3 hours")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, intervals)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.intervalSpinner.adapter = adapter

        binding.intervalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val interval = intervals[position]
                Toast.makeText(requireContext(), "Notification interval set to $interval", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.editTextGoal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    goalAmount = s.toString().toInt()
                    updateProgress(authToken.toString())
                }
            }
        })

        val cupSizesWithLabels = arrayOf("100 ml - Small cup", "200 ml - Medium cup", "300 ml - Large cup")
        val cupAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cupSizesWithLabels)
        cupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCupSize.adapter = cupAdapter

        binding.buttonSelectNotificationInterval.setOnClickListener {
            if (binding.notificationIntervalLayout.visibility == View.VISIBLE) {
                binding.notificationIntervalLayout.visibility = View.GONE
            } else {
                binding.notificationIntervalLayout.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    private fun updateProgress(authToken: String) {
        val progress = progress + ((totalWaterDrank.toFloat() / goalAmount.toFloat()) * 100)
        binding.progressIndicator.progress = progress.toInt()
        binding.textTotalWaterDrank.text = "Total Water Drank: " + (current_water + totalWaterDrank) + "ml"

        val percentText = String.format(Locale.getDefault(), "%.1f%%", progress)
        binding.progressIndicator.contentDescription = percentText
        createWater(authToken, totalWaterDrank.toString())

    }

    private fun indicateDrink(authToken: String) {
        val selectedCupSizeString = binding.spinnerCupSize.selectedItem.toString()
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
                binding.textDailyGoalIndicator.visibility = View.GONE
            }
        } else {
            Toast.makeText(requireContext(), "Invalid cup size input", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExceededGoalSnackbar() {
        val snackbar = Snackbar.make(
            requireView(),
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
        binding.textDailyGoalIndicator.visibility = View.GONE
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
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                requireContext(),
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
                                requireContext(),
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT)
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
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {

                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Toast.makeText(
                                requireContext(),
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val descriptionText = "Channel for Notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(com.carecrafter.R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification")
            .setContentText("This is a notification message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId++, builder.build())
        }
    }

    private fun getWaterHistory(authToken: String) {
        ApiClient.instance.getWaterHistory("Bearer $authToken").enqueue(object : Callback<WaterHistoryApi> {
            override fun onResponse(call: Call<WaterHistoryApi>, response: Response<WaterHistoryApi>) {
                if (response.isSuccessful) {
                    val waterData = response.body()
                    if (waterData != null) {
                        updateWater(waterData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                }
            }
            override fun onFailure(call: Call<WaterHistoryApi>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get user info", t)
            }
        })
    }

    fun updateWater(waterData: WaterHistoryApi){
        binding.textTotalWaterDrank.text = "Total Water Drank: ${waterData.totalWater} ml"
        current_water = waterData.totalWater.toInt()
        binding.progressIndicator.progress = waterData.totalWater.toInt()
        progress = waterData.totalWater.toInt()
    }
}