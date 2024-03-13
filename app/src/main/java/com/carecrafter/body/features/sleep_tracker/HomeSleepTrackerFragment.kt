package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.body.adapters.SleepHistoryAdapter
import com.carecrafter.body.adapters.WaterHistoryAdapter
import com.carecrafter.body.features.water_intake.WaterIntakeBActivity
import com.carecrafter.databinding.SleepTrackerHomeBinding
import com.carecrafter.models.SleepHistory
import com.carecrafter.models.SleepHistoryApi
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.WaterHistory
import com.carecrafter.models.WaterHistoryApi
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeSleepTrackerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: SleepTrackerHomeBinding
    private var progress = 0
    private lateinit var sleepHistoryAdapter: SleepHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getSleepTime(it) }

        sleepHistoryAdapter = SleepHistoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = sleepHistoryAdapter
        getSleepHistory(authToken.toString())
        binding.setGoalBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_goalSetSleepTrackerFragment)
        }
//        val totalSleep = binding.tvTotalSleep.text.toString().toInt()
//        val score = binding.tvScore.text.toString().toInt()
//
//        binding.scorePercent.text = "$score%"
//
//        binding.qualityBar.progress = (progress + score)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            val intent = Intent(activity, BodyActivity::class.java)
            startActivity(intent)
        }
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
                    Toast.makeText(requireContext(), "Failed to get sleep info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SleepsApi>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get logs info", t)
                Toast.makeText(requireContext(), "Failed to get logs info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateScore(scoreData: SleepsApi){
        binding.tvTotalSleep.text = scoreData.totalSleeps.toString()
        binding.tvScore.text = scoreData.score
    }

    private fun getSleepHistory(authToken: String) {
        ApiClient.instance.getSleepHistory("Bearer $authToken").enqueue(object : Callback<List<SleepHistoryApi>> {
            override fun onResponse(call: Call<List<SleepHistoryApi>>, response: Response<List<SleepHistoryApi>>) {
                if (response.isSuccessful) {
                    val sleepHistoryApi = response.body() ?: emptyList()
                    val entries = sleepHistoryApi.map { sleepHistoryApi ->
                        SleepHistory(
                            score = sleepHistoryApi.score,
                            sleeps = sleepHistoryApi.sleeps,
                            date = sleepHistoryApi.date,
                        )
                    }
                    sleepHistoryAdapter.updateData(entries)
                } else {
                    Toast.makeText(requireContext(), "Failed to get step history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SleepHistoryApi>>, t: Throwable) {
                Log.e("HomeSleepFragment", "Failed to get Sleep history", t)
                Toast.makeText(requireContext(), "Failed to get Sleep history", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
