package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.FragmentResultSleepTrackerBinding
import com.carecrafter.models.Alarm
import com.carecrafter.models.SleepScoreLogs
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResultSleepTrackerFragment : Fragment() {
    private lateinit var binding: FragmentResultSleepTrackerBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultSleepTrackerBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getAlarmInfo(it) }
        authToken?.let { getScore(it) }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_resultSleepTrackerFragment_to_homeSleepTrackerFragment)
        }
//        val input = binding.tvTimeGoal.text.toString().toInt()
//        val scoreDivide = (input * 60) * 60
//        val rate = ( / scoreDivide) * 100
//        val hours = timerSeconds / 3600
//        val minutes = (timerSeconds % 3600) / 60
//        val seconds = timerSeconds % 60
        return binding.root
    }
    private fun getAlarmInfo(authToken: String) {
        ApiClient.instance.getAlarm("Bearer $authToken").enqueue(object : Callback<Alarm> {
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
            binding.tvTimeGoal.text = alarmData.daily_goal
        }
    }
    private fun getScore(authToken: String) {
        ApiClient.instance.getScore("Bearer $authToken").enqueue(object : Callback<SleepScoreLogs> {
            override fun onResponse(call: Call<SleepScoreLogs>, response: Response<SleepScoreLogs>) {
                if (response.isSuccessful) {
                    val scoreData = response.body()
                    if (scoreData != null) {
                        updateScore(scoreData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SleepScoreLogs>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get logs info", t)
                Toast.makeText(requireContext(), "Failed to get logs info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateScore(scoreData: SleepScoreLogs?){
        if (scoreData != null) {
            binding.tvTimerSeconds.text = scoreData.total_time
        }
    }
}