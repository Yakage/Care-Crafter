package com.carecrafter.body

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.features.BmiCalcuActivity
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.body.features.water_intake.WaterIntakeBActivity
import com.carecrafter.databinding.BodyHomeBinding
import com.carecrafter.models.BMI
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.StepHistoryApi
import com.carecrafter.models.User
import com.carecrafter.models.WaterHistoryApi
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
        authToken?.let { getBMI(it) }
        authToken?.let { getWaterHistory(it) }
        binding = BodyHomeBinding.inflate(inflater, container, false)

        binding.sleepFT.setOnClickListener {
            val intent = Intent(activity, SleepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.stepFT.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCurrentUpdatingStepTrackerFragment())
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
            val url = "https://carecrafter-e36f7bd1d791.herokuapp.com/" //put the fuckining website here
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
            when (userData.avatar) {
                1 -> binding.profilePicture.setImageResource(R.drawable.avatarone)
                2 -> binding.profilePicture.setImageResource(R.drawable.avatartwo)
                3 -> binding.profilePicture.setImageResource(R.drawable.avatarthree)
                4 -> binding.profilePicture.setImageResource(R.drawable.avatarfour)
                5 -> binding.profilePicture.setImageResource(R.drawable.avatarfive)
                6 -> binding.profilePicture.setImageResource(R.drawable.avatarsix)
                7 -> binding.profilePicture.setImageResource(R.drawable.avatarseven)
                8 -> binding.profilePicture.setImageResource(R.drawable.avataeight)
                else -> {
                    binding.profilePicture.setImageResource(R.drawable.boy_unpressed)
                }
            }
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
                Toast.makeText(requireContext(), "Failed to get sleep info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateScore(scoreData: SleepsApi){
        if (scoreData != null){
            binding.tvTotalSleep.text = scoreData.totalSleeps
            binding.tvScore.text = scoreData.score
        }else{
            binding.tvTotalSleep.text = "0"
            binding.tvScore.text = "0"
        }

    }

    private fun getStepHistory(authToken: String) {
        ApiClient.instance.getStepHistory("Bearer $authToken").enqueue(object : Callback<StepHistoryApi> {
            override fun onResponse(call: Call<StepHistoryApi>, response: Response<StepHistoryApi>) {
                if (response.isSuccessful) {
                    val stepData = response.body()
                    if (stepData != null) {
                        updateStep(stepData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get step info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<StepHistoryApi>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get step info", t)
                Toast.makeText(requireContext(), "Failed to get step info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateStep(stepData: StepHistoryApi){
        val blue = ContextCompat.getColor(requireContext(), R.color.blue)
        val currentSteps = stepData.current_steps ?: "0" // Safe access to current_steps
        val dailyGoal = stepData.daily_goal ?: "0"
        if (stepData != null) {
            binding.tvStepGoal.text = "Goal: " + stepData.current_steps + " / " + stepData.daily_goal
            binding.circularProgressBar.apply {
                progressMax = dailyGoal.toFloat()
                setProgressWithAnimation(currentSteps.toFloat(), 1000)
                progressBarWidth = 5f
                backgroundProgressBarWidth = 5f
                progressBarColor = blue
            }
        }
    }

    private fun getBMI(authToken: String) {
        ApiClient.instance.getBMI("Bearer $authToken").enqueue(object : Callback<BMI> {
            override fun onResponse(call: Call<BMI>, response: Response<BMI>) {
                if (response.isSuccessful) {
                    val bmiData = response.body()
                    if (bmiData != null) {
                        updateBMI(bmiData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get bmi info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BMI>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get bmi info", t)
                Toast.makeText(requireContext(), "Failed to get bmi info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateBMI(bmiData: BMI){
        binding.tvBmi.text = bmiData.bmi
    }

    private fun getWaterHistory(authToken: String) {
        ApiClient.instance.getWaterHistory("Bearer $authToken").enqueue(object : Callback<WaterHistoryApi> {
            override fun onResponse(call: Call<WaterHistoryApi>, response: Response<WaterHistoryApi>) {
                if (response.isSuccessful) {
                    val waterData = response.body()
                    if (waterData != null) {
                        updateWater(waterData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get water info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<WaterHistoryApi>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get water info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateWater(waterData: WaterHistoryApi){
        binding.tvWaterTotal.text = waterData.totalWater
    }
}
