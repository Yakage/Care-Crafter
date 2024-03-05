package com.carecrafter.body.features.step_tracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Editable
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.StepTrackerHomeBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class HomeStepTrackerFragment : Fragment(), SensorEventListener {
    private lateinit var binding: StepTrackerHomeBinding
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var stepHistoryAdapter: ArrayAdapter<String> // Define ArrayAdapter for step history
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = StepTrackerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stepHistoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        binding.listViewStepHistory.adapter = stepHistoryAdapter
        resetSteps()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Register BroadcastReceiver for date change
        val filter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        requireContext().registerReceiver(dateChangeReceiver, filter)
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }

        binding.btStart.setOnClickListener {
            startStepCounting()
        }

        binding.btStop.setOnClickListener {
            stopStepCounting(authToken.toString())
        }

        binding.btReset.setOnClickListener {
            resetStepCount()
        }

        binding.btStatistics.setOnClickListener {
            findNavController().navigate(R.id.action_homeStepTrackerFragment_to_statisticStepTrackerFragment)
        }

        // Add click listener to the "Set Goal" button
        binding.setgoal.setOnClickListener {
            if (binding.etGoal.text.toString().isEmpty()){
                binding.etGoal.error = "Set a Goal"
                binding.etGoal.requestFocus()
            } else {
                setGoal()
            }
        }
    }

    // Broadcast receiver for date change
    private val dateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            resetStepCount()
        }
    }

    private fun startStepCounting() {
        totalSteps = 0f
        previousTotalSteps = 0f
        binding.btStart.isEnabled = false
        binding.btStop.isEnabled = true
        binding.btReset.isEnabled = false

        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
        // Reset initial total steps value
    }

    private fun stopStepCounting(authToken: String) {
        binding.btStart.isEnabled = true
        binding.btStop.isEnabled = false
        binding.btReset.isEnabled = true

        running = false
        sensorManager?.unregisterListener(this)
        // Save total steps to history when stop button is clicked
        saveStepToHistory(authToken, totalSteps.toInt())
    }

    private fun resetStepCount() {

        binding.setGoalLayout.visibility = View.VISIBLE
        binding.setButtonsLayout.visibility = View.GONE

        binding.btStart.isEnabled = true
        binding.btStop.isEnabled = false
        binding.btReset.isEnabled = false

        previousTotalSteps = totalSteps
        binding.tvTotal.text = "Total Steps"
        binding.tvCount.text = "0"
        binding.etGoal.text = Editable.Factory.getInstance().newEditable("")
        stepHistoryAdapter.clear()
        Toast.makeText(requireContext(), "Step count reset", Toast.LENGTH_SHORT).show()
    }

    private fun setGoal() {
        val goal = binding.etGoal.text.toString()

        binding.setGoalLayout.visibility = View.GONE
        binding.setButtonsLayout.visibility = View.VISIBLE

        binding.btStart.isEnabled = true
        binding.btStop.isEnabled = false
        binding.btReset.isEnabled = true

        if (goal.isNotEmpty()) {
            // Update tv_total with the entered goal
            binding.tvTotal.text = "$goal"
            Toast.makeText(requireContext(), "Goal set successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please enter a valid goal", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            binding.tvCount.text = currentSteps.toString()
        }
    }

    private fun resetSteps() {
        binding.tvTotal.setOnLongClickListener {
            resetStepCount()
            true
        }
    }

    private fun saveStepToHistory(authToken: String, steps: Int) {
        // Add steps to step history
        val goal = binding.tvTotal.text.toString().toInt()
        stepHistoryAdapter.add("Steps: $steps - Goal: $goal")

        // Call your API to save steps to your online database here
        // Example: YourApiService.saveStepToDatabase(steps)
        createStepHistoryData(authToken, "$steps / $goal")
        createStepSata(authToken,steps.toString())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We do not have to write anything in this function for this app
    }

    private fun createStepHistoryData(authToken: String, step_history: String){
        val createStepHistoryDataJson =
            "{\"authToken\":\"$authToken\",\"step_history\":\"$step_history\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createStepHistoryDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createStepHistory(
                "Bearer $authToken",
                step_history
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
    private fun createStepSata(authToken: String, steps: String){
        val createStepDataJson =
            "{\"authToken\":\"$authToken\",\"steps\":\"$steps\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createStepDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createStep(
                "Bearer $authToken",
                steps
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
}
