package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.carecrafter.R
import com.carecrafter.body.adapters.SleepTrackerLeaderboardAdapter
import com.carecrafter.databinding.SleepTrackerLeaderboardBinding
import com.carecrafter.models.LeaderboardForSleepTracker
import com.carecrafter.models.SleepsApi
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardSleepTrackerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var leaderboardAdapter: SleepTrackerLeaderboardAdapter
    private lateinit var binding: SleepTrackerLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SleepTrackerLeaderboardBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)
        val black = ContextCompat.getColor(requireContext(), R.color.black)

        leaderboardAdapter = SleepTrackerLeaderboardAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = leaderboardAdapter
        if (authToken != null && authToken.isNotEmpty()) {
            binding.tvDaily.setTextColor(blue)
            getDailySleeps(authToken.toString())
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        if (authToken != null && authToken.isNotEmpty()) {
            binding.daily.setOnClickListener {
                binding.tvDaily.setTextColor(blue)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(black)
                getDailySleeps(authToken.toString())
            }

            binding.weekly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(blue)
                binding.tvMonthly.setTextColor(black)
                getWeeklySleeps(authToken.toString())
            }

            binding.monthly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(blue)
                getMonthlySleeps(authToken.toString())
            }
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getDailySleeps(authToken: String) {
        ApiClient.instance.showDailySleep("Bearer $authToken").enqueue(object :
            Callback<List<SleepsApi>> {
            override fun onResponse(call: Call<List<SleepsApi>>, response: Response<List<SleepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { sleepsApi ->
                        LeaderboardForSleepTracker(
                            name = sleepsApi.name,
                            sleeps = sleepsApi.totalSleeps.toString()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SleepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWeeklySleeps(authToken: String) {
        ApiClient.instance.showWeeklySleep("Bearer $authToken").enqueue(object :
            Callback<List<SleepsApi>> {
            override fun onResponse(call: Call<List<SleepsApi>>, response: Response<List<SleepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { sleepsApi ->
                        LeaderboardForSleepTracker(
                            name = sleepsApi.name,
                            sleeps = sleepsApi.totalSleeps.toString()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SleepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getMonthlySleeps(authToken: String) {
        ApiClient.instance.showMonthlySleep("Bearer $authToken").enqueue(object :
            Callback<List<SleepsApi>> {
            override fun onResponse(call: Call<List<SleepsApi>>, response: Response<List<SleepsApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { sleepsApi ->
                        LeaderboardForSleepTracker(
                            name = sleepsApi.name,
                            sleeps = sleepsApi.totalSleeps.toString()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SleepsApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
