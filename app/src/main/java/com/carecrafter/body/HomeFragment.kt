package com.carecrafter.body

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.carecrafter.R
import com.carecrafter.body.features.BmiCalcuActivity
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.body.features.StepTrackerActivity
import com.carecrafter.body.features.water_intake.WaterIntakeBActivity
import com.carecrafter.databinding.BodyHomeBinding
import com.carecrafter.models.Alarm
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.StepHistory
import com.carecrafter.models.User
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: BodyHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getUserInfo(it) }
        authToken?.let { getSleepTime(it) }
        authToken?.let { getStepHistory(it) }
        binding = BodyHomeBinding.inflate(inflater, container, false)

        binding.sleepFT.setOnClickListener {
            val intent = Intent(activity, SleepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.stepFT.setOnClickListener {
            val intent = Intent(activity, StepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.waterFT.setOnClickListener {
            val intent = Intent(activity, WaterIntakeBActivity::class.java)
            startActivity(intent)
        }
        binding.bmiFT.setOnClickListener {
            val intent = Intent(activity, BmiCalcuActivity::class.java)
            startActivity(intent)
        }
        binding.btnVisitWebsite.setOnClickListener {
            val url = "https://example.com" //put the fuckining website here
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


        return binding.root
    }
    private fun getUserInfo(authToken: String) {
        ApiClient.instance.getUser("Bearer $authToken").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        updateInfo(userData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun updateInfo(userData: User){
        if (userData != null) {
            binding.tvFullName.text = Editable.Factory.getInstance().newEditable(userData.name)
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

    private fun getStepHistory(authToken: String) {
        ApiClient.instance.getStepHistory("Bearer $authToken").enqueue(object : Callback<StepHistory> {
            override fun onResponse(call: Call<StepHistory>, response: Response<StepHistory>) {
                if (response.isSuccessful) {
                    val stepData = response.body()
                    if (stepData != null) {
                        updateStep(stepData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<StepHistory>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateStep(stepData: StepHistory){
        if (stepData != null) {
            val blue = ContextCompat.getColor(requireContext(), R.color.blue)
            binding.tvStepGoal.text = "Goal: " + stepData.current_steps + " / " + stepData.daily_goal
            binding.circularProgressBar.apply {
                progressMax = stepData.daily_goal.toFloat()
                setProgressWithAnimation(stepData.current_steps.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 5f
                progressBarColor = blue
            }
        }
    }
}
