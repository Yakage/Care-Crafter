package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerHomeBinding
import com.carecrafter.models.SleepScoreLogs
import com.carecrafter.models.SleepsApi
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeSleepTrackerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: SleepTrackerHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getSleepTime(it) }

        binding.setGoalBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_goalSetSleepTrackerFragment)
        }

        return binding.root
    }

    private fun getSleepTime(authToken: String) {
        ApiClient.instance.getSleepTime("Bearer $authToken").enqueue(object : Callback<SleepsApi> {
            override fun onResponse(call: Call<SleepsApi>, response: Response<SleepsApi>) {
                if (response.isSuccessful) {
                    val scoreData = response.body()
                    if (scoreData != null) {
                        updateScore(scoreData)
                    }

                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SleepsApi>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get logs info", t)
                Toast.makeText(requireContext(), "Failed to get logs info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateScore(scoreData: SleepsApi){
        binding.tvTotalSleep.text = scoreData.totalSleeps
        binding.tvScore.text = scoreData.score
    }
}
