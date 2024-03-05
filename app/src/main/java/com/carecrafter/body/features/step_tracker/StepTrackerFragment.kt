//import android.content.Context
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.carecrafter.databinding.FragmentStepTrackerBinding
//import com.carecrafter.databinding.StepTrackerHomeBinding
//
//
//class StepTrackerFragment : Fragment(), SensorEventListener {
//    private lateinit var binding: StepTrackerHomeBinding
//    private lateinit var sensorManager: SensorManager
//    private var stepCount = 0
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = StepTrackerHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize sensor manager
//        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
//
//        // Set listeners
//        binding.setgoal.setOnClickListener { setGoal() }
//        binding.btStart.setOnClickListener { startTracking() }
//        binding.btStop.setOnClickListener { stopTracking() }
//        binding.btReset.setOnClickListener { resetTracking() }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        // Register the step counter sensor
//        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        stepCounterSensor?.let {
//            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // Unregister the sensor to save battery
//        sensorManager.unregisterListener(this)
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        // Do nothing
//    }
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        // Update step count when sensor data changes
//        event?.let {
//            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
//                stepCount = it.values[0].toInt()
//                updateStepCount()
//            }
//        }
//    }
//
//    private fun setGoal() {
//        val goal = binding.etGoal.text.toString().toIntOrNull()
//        if (goal != null) {
//            // Set the daily step goal
//            // You can implement this part according to your requirement
//        } else {
//            // Handle invalid input
//        }
//    }
//
//    private fun startTracking() {
//        // Start tracking steps
//        // In this implementation, step counting is automatic once the sensor is registered
//    }
//
//    private fun stopTracking() {
//        // Stop tracking steps
//        // In this implementation, step counting is continuous until the sensor is unregistered
//    }
//
//    private fun resetTracking() {
//        // Reset step count
//        stepCount = 0
//        updateStepCount()
//    }
//
//    private fun updateStepCount() {
//        binding.tvCount.text = stepCount.toString()
//        // Update progress bar or any other UI element here based on step count
//    }
//}
