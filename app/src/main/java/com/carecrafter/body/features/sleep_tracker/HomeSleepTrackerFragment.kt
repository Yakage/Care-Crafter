package com.carecrafter.body.features.sleep_tracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.SleepTrackerHomeBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeSleepTrackerFragment : Fragment() {

    private lateinit var binding: SleepTrackerHomeBinding
    private var isRunning = false
    private var timerSeconds = 0

    private val handler = Handler(Looper.getMainLooper())
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)

        binding.ivAlarm.setOnClickListener{
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_settingsSleepTrackerFragment)
        }
        binding.startBtn.setOnClickListener{
            startTimer()
        }
        binding.stopBtn.setOnClickListener{
            stopTimer()
        }
        binding.resetBtn.setOnClickListener{
            resetTimer()
        }
        binding.rateBtn.setOnClickListener {
            calculator()
        }

        // Date and Time for the TextView
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG).format(calendar)
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
            binding.resetBtn.isEnabled = true
            binding.rateBtn.isEnabled = false
        }
    }
    private fun stopTimer(){
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.startBtn.isEnabled = true
            binding.startBtn.text = "Resume"
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = true
            binding.rateBtn.isEnabled = true
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
        binding.rateBtn.isEnabled = false
    }

    private fun calculator() {
        // The 28800 is Seconds if which converted its 8 Hours
        val rate = (timerSeconds / 28800) * 100
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
                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes hrs of sleep at ${score.second}\n"
            }
            binding.scoreLogs.text = scoreHistory
        }

        else {
            val currentTime = Calendar.getInstance().time
            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            scoreHistoryList.add(Pair(rate, timeString))

            var scoreHistory = "Sleep Score History:\n"
            for (score in scoreHistoryList) {
                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes hrs of sleep at ${score.second}\n"
            }
            binding.scoreLogs.text = scoreHistory
        }
    }
}
