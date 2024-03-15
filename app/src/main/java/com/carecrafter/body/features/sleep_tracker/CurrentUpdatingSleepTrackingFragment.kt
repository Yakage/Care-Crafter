package com.carecrafter.body.features.sleep_tracker

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
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.SleepTrackingBinding
import com.carecrafter.databinding.SleepTrackingCurrentUpdatingBinding
import com.carecrafter.models.Alarm
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CurrentUpdatingSleepTrackingFragment : Fragment() {

    private lateinit var binding: SleepTrackingCurrentUpdatingBinding
    private var isRunning = false
    private var timerSeconds = 0
    private val channelId = "notification_channel"
    private var notificationId = 0
    var input = 0

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var sharedPreferences: SharedPreferences
    private val runnable = object : Runnable {
        override fun run() {
            timerSeconds++
            val hours = timerSeconds / 3600
            val minutes = (timerSeconds % 3600) / 60
            val seconds = timerSeconds % 60

            val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            binding.timeTV.text = time

            handler.postDelayed(this, 1000)
        }
    }
    private val scoreHistoryList = mutableListOf<Pair<Int, String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SleepTrackingCurrentUpdatingBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getAlarmInfo(it) }

        createNotificationChannel()

        binding.startBtn.setOnClickListener{
            handler.postDelayed({ sendNotification() }, (input * 60 * 60 * 1000).toLong()) // Input = Hour
            startTimer()
        }
        binding.stopBtn.setOnClickListener{
            stopTimer()
        }
        binding.resetBtn.setOnClickListener{
            resetTimer()
        }
        binding.endBtn.setOnClickListener {
            calculator(authToken.toString())
            findNavController().navigate(CurrentUpdatingSleepTrackingFragmentDirections.actionCurrentUpdatingSleepTrackingFragmentToCurrentResultSleepTrackerFragment2())
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigate(CurrentUpdatingSleepTrackingFragmentDirections.actionCurrentUpdatingSleepTrackingFragmentToHomeFragment())
        }

        // Date and Time for the TextView
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)
        binding.currentTime.text = "$timeFormat"
        binding.currentDate.text = "$dateFormat"

        binding.ivBack.setOnClickListener {
            val intent = Intent(activity, BodyActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

        private fun startTimer(){
        if (!isRunning){
            handler.postDelayed(runnable, 1000)
            isRunning = true

            binding.startBtn.isEnabled = false
            binding.stopBtn.isEnabled = true
            binding.endBtn.isEnabled = false
            binding.resetBtn.isEnabled = true
        }
    }

    private fun stopTimer(){
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.startBtn.isEnabled = true
            binding.startBtn.text = "Resume"
            binding.stopBtn.isEnabled = false
            binding.endBtn.isEnabled = true
            binding.resetBtn.isEnabled = true
        }
    }
    private fun resetTimer(){
        stopTimer()

        timerSeconds = 0
        binding.timeTV.text = "00:00:00"

        binding.startBtn.isEnabled = true
        binding.stopBtn.isEnabled = false
        binding.startBtn.text = "Start"
        binding.resetBtn.isEnabled = false
        binding.endBtn.isEnabled = false
    }

    //private fun calculator(authToken: String)
    private fun calculator(authToken: String) {
        binding.startBtn.isEnabled = true
        binding.stopBtn.isEnabled = false
        binding.resetBtn.isEnabled = false
        binding.endBtn.isEnabled = false
        // The 28800 is Seconds if which converted its 8 Hours

        val input = input
        val scoreDivide = (input * 60) * 60
        val rate = (timerSeconds / scoreDivide) * 100
        val hours = timerSeconds / 3600
        val minutes = (timerSeconds % 3600) / 60
        val seconds = timerSeconds % 60

        // I use if statement to prevent the score going over 100
        if (rate >= 100) {
            val rate = 100
            val currentTime = Calendar.getInstance().time
            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            scoreHistoryList.add(Pair(rate, timeString))

            var scoreHistory = "Sleep Score History:\n"
            for (score in scoreHistoryList) {
                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes.$seconds hrs of sleep at ${score.second}\n"
            }
            val scoreData = "Score: $rate Time: $timeString"
            //createScore(authToken, scoreData)
            binding.scoreLogs.text = scoreHistory.toString()
            createScore(authToken, scoreData, timerSeconds.toString() )
        }

        else {
            val currentTime = Calendar.getInstance().time
            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            scoreHistoryList.add(Pair(rate, timeString))

            var scoreHistory = "Sleep Score History:\n"
            for (score in scoreHistoryList) {
                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes.$seconds hrs of sleep at ${score.second}\n"
            }
            val scoreData = "Score: $rate Time: $timeString"
            //createScore(authToken, scoreData)
            binding.scoreLogs.text = scoreHistory
            createScore(authToken, scoreData, timerSeconds.toString() )

        }
    }
    private fun createScore(authToken: String, score: String, totalTime: String){

        val createScoreDataJson =
            "{\"authToken\":\"$authToken\",\"score\":\"$score\",\"totalTime\":\"$totalTime\"}"


        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createScoreDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createScore(
                "Bearer $authToken",
                score,
                totalTime
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

    private fun getAlarmInfo(authToken: String) {
        ApiClient.instance.getAlarm("Bearer $authToken").enqueue(object : Callback<Alarm>{
            override fun onResponse(call: Call<Alarm>, response: Response<Alarm>) {
                if (response.isSuccessful) {
                    val alarmData = response.body()
                    if (alarmData != null) {
                        updateInfo(alarmData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Alarm>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateInfo(alarmData: Alarm){
        if (alarmData != null) {
            if (alarmData.daily_goal.toInt() > 1){
                binding.alarm.text = "Alarm in ${alarmData.daily_goal} hours"
            }else{
                binding.alarm.text = "Alarm in ${alarmData.daily_goal} hour"
            }

            input = alarmData.daily_goal.toInt()
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
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Notification")
            .setContentText("This is a notification message.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId++, builder.build())
        }
    }
}