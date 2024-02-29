package com.carecrafter.body.features.step_tracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.StepTrackerHomeBinding

class HomeStepTrackerFragment : Fragment(), SensorEventListener {
    private lateinit var binding: StepTrackerHomeBinding
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var stepHistoryAdapter: ArrayAdapter<String> // Define ArrayAdapter for step history

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
        loadData()
        resetSteps()

        // Initialize step history adapter
        stepHistoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.listViewStepHistory.adapter = stepHistoryAdapter

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }

        binding.btStart.setOnClickListener {
            startStepCounting()
        }

        binding.btStop.setOnClickListener {
            stopStepCounting()
        }

        binding.btReset.setOnClickListener {
            resetStepCount()
        }

        // Add click listener to the "Set Goal" button
        binding.setgoal.setOnClickListener {
            setGoal()
        }
    }

    private fun startStepCounting() {

        binding.btStart.isEnabled = false
        binding.btStop.isEnabled = true
        binding.btReset.isEnabled = true

        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun stopStepCounting() {

        binding.btStart.isEnabled = true
        binding.btStop.isEnabled = false
        binding.btReset.isEnabled = true

        running = false
        sensorManager?.unregisterListener(this)
        // Save total steps to history when stop button is clicked
        saveStepToHistory(totalSteps.toInt())
    }

    private fun resetStepCount() {

        binding.etGoal.visibility = View.VISIBLE
        binding.setgoal.visibility = View.VISIBLE

        binding.btStart.isEnabled = true
        binding.btStop.isEnabled = false
        binding.btReset.isEnabled = false


        previousTotalSteps = totalSteps
        binding.tvTotal.text = "Total Step"
        saveData()
        updateStepHistory() // Update step history when reset button is clicked
        Toast.makeText(requireContext(), "Step count reset", Toast.LENGTH_SHORT).show()
    }

    private fun setGoal() {
        val goal = binding.etGoal.text.toString()

        binding.etGoal.visibility = View.INVISIBLE
        binding.setgoal.visibility = View.INVISIBLE

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

    private fun saveStepToHistory(steps: Int) {
        // Add steps to step history
        stepHistoryAdapter.add("Steps: $steps")
    }

    private fun updateStepHistory() {
        // Clear step history and reload saved steps
        stepHistoryAdapter.clear()
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        previousTotalSteps = savedNumber ?: 0f
        saveStepToHistory(previousTotalSteps.toInt())
    }

    private fun saveData() {
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)
        previousTotalSteps = savedNumber ?: 0f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We do not have to write anything in this function for this app
    }


}
