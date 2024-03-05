package com.carecrafter.body.features.step_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carecrafter.R
import com.carecrafter.body.adapters.LeaderboardAdapter
import com.carecrafter.databinding.LeaderboardForStepTrackerBinding
import com.carecrafter.databinding.StepTrackerLeaderboardBinding
import com.carecrafter.models.StepsApi
import com.carecrafter.models.LeaderboardForStepTracker
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderBoardStepTrackerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private lateinit var binding: StepTrackerLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StepTrackerLeaderboardBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)
        val black = ContextCompat.getColor(requireContext(), R.color.black)

        leaderboardAdapter = LeaderboardAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = leaderboardAdapter
        if (authToken != null && authToken.isNotEmpty()) {
            binding.tvDaily.setTextColor(blue)
            getDailySteps(authToken.toString())
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        if (authToken != null && authToken.isNotEmpty()) {
            binding.daily.setOnClickListener {
                binding.tvDaily.setTextColor(blue)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(black)
                getDailySteps(authToken.toString())
            }

            binding.weekly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(blue)
                binding.tvMonthly.setTextColor(black)
                getWeeklySteps(authToken.toString())
            }

            binding.monthly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(blue)
                getMonthlySteps(authToken.toString())
            }
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getDailySteps(authToken: String) {
        ApiClient.instance.showDailySteps("Bearer $authToken").enqueue(object : Callback<List<StepsApi>> {
            override fun onResponse(call: Call<List<StepsApi>>, response: Response<List<StepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { dailyStepsApi ->
                        LeaderboardForStepTracker(
                            name = dailyStepsApi.name,
                            steps = dailyStepsApi.totalSteps
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWeeklySteps(authToken: String) {
        ApiClient.instance.showWeeklySteps("Bearer $authToken").enqueue(object : Callback<List<StepsApi>> {
            override fun onResponse(call: Call<List<StepsApi>>, response: Response<List<StepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { dailyStepsApi ->
                        LeaderboardForStepTracker(
                            name = dailyStepsApi.name,
                            steps = dailyStepsApi.totalSteps
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMonthlySteps(authToken: String) {
        ApiClient.instance.showMonthlySteps("Bearer $authToken").enqueue(object : Callback<List<StepsApi>> {
            override fun onResponse(call: Call<List<StepsApi>>, response: Response<List<StepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { dailyStepsApi ->
                        LeaderboardForStepTracker(
                            name = dailyStepsApi.name,
                            steps = dailyStepsApi.totalSteps
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
