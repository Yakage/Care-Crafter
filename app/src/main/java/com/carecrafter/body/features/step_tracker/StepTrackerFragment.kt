package com.carecrafter.body.features.step_tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carecrafter.R

class StepTrackerFragment : Fragment() {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private lateinit var stepCountTextView: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button
    private var isRunning: Boolean = false
    private var stepCount: Int = 0

    private val stepSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (isRunning) {
                stepCount = event.values[0].toInt()
                updateStepCount(stepCount)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_step_tracker, container, false)

        stepCountTextView = view.findViewById(R.id.stepCountTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            ?: run {
                Toast.makeText(requireContext(), "Step counter sensor not available on this device", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
                return view
            }

        stepCountTextView.text = "Click Start"

        startButton.setOnClickListener {
            isRunning = true
            stepCountTextView.text = "Tracking Steps..."
        }

        stopButton.setOnClickListener {
            if (isRunning) {
                isRunning = false
                stepCountTextView.text = "Step Count: $stepCount"
            } else {
                Toast.makeText(requireContext(), "Please start tracking steps first", Toast.LENGTH_SHORT).show()
            }
        }

        resetButton.setOnClickListener {
            stepCount = 0
            stepCountTextView.text = "Click Start"
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (::stepSensor.isInitialized) {
            sensorManager.registerListener(stepSensorListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(requireContext(), "Step counter sensor not available on this device", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::stepSensor.isInitialized) {
            sensorManager.unregisterListener(stepSensorListener)
        }
    }

    private fun updateStepCount(steps: Int) {
        if (!isRunning) {
            stepCountTextView.text = "Step Count: $steps"
        } else {
            stepCountTextView.text = "Tracking Steps..."
        }
    }
}
