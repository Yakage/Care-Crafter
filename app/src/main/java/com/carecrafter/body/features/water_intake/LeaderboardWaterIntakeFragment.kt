package com.carecrafter.body.features.water_intake

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
import com.carecrafter.body.adapters.WaterIntakeLeaderboardAdapter
import com.carecrafter.databinding.WaterIntakeLeaderboardBinding
import com.carecrafter.models.LeaderboardForWaterIntake
import com.carecrafter.models.WaterApi
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardWaterIntakeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var leaderboardAdapter: WaterIntakeLeaderboardAdapter
    private lateinit var binding: WaterIntakeLeaderboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WaterIntakeLeaderboardBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)
        val black = ContextCompat.getColor(requireContext(), R.color.black)

        leaderboardAdapter = WaterIntakeLeaderboardAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = leaderboardAdapter
        if (authToken != null && authToken.isNotEmpty()) {
            binding.tvDaily.setTextColor(blue)
            getDailyWaterIntake(authToken.toString())
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        if (authToken != null && authToken.isNotEmpty()) {
            binding.daily.setOnClickListener {
                binding.tvDaily.setTextColor(blue)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(black)
                getDailyWaterIntake(authToken.toString())
            }

            binding.weekly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(blue)
                binding.tvMonthly.setTextColor(black)
                getWeeklyWaterIntake(authToken.toString())
            }

            binding.monthly.setOnClickListener {
                binding.tvDaily.setTextColor(black)
                binding.tvWeekly.setTextColor(black)
                binding.tvMonthly.setTextColor(blue)
                getMonthlyWaterIntake(authToken.toString())
            }
        } else {
            Toast.makeText(requireContext(), "Auth token not found", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getDailyWaterIntake(authToken: String) {
        ApiClient.instance.showDailyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterApi>> {
            override fun onResponse(call: Call<List<WaterApi>>, response: Response<List<WaterApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { waterApi ->
                        LeaderboardForWaterIntake(
                            name = waterApi.name,
                            water = waterApi.totalWater.toInt()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                }
            }

            override fun onFailure(call: Call<List<WaterApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
            }
        })
    }

    private fun getWeeklyWaterIntake(authToken: String) {
        ApiClient.instance.showWeeklyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterApi>> {
            override fun onResponse(call: Call<List<WaterApi>>, response: Response<List<WaterApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { waterApi ->
                        LeaderboardForWaterIntake(
                            name = waterApi.name,
                            water = waterApi.totalWater.toInt()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                }
            }

            override fun onFailure(call: Call<List<WaterApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
            }
        })
    }

    private fun getMonthlyWaterIntake(authToken: String) {
        ApiClient.instance.showMonthlyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterApi>> {
            override fun onResponse(call: Call<List<WaterApi>>, response: Response<List<WaterApi>>) {
                if (response.isSuccessful) {
                    val entries = response.body()?.map { waterApi ->
                        LeaderboardForWaterIntake(
                            name = waterApi.name,
                            water = waterApi.totalWater.toInt()
                        )
                    } ?: emptyList()
                    leaderboardAdapter.updateData(entries)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                }
            }

            override fun onFailure(call: Call<List<WaterApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
            }
        })
    }
}
