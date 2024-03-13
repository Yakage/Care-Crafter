package com.carecrafter.body.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ImageViewCompat.setImageTintList
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.databinding.AchievementsHomeBinding
import com.carecrafter.models.SleepsApi
import com.carecrafter.models.StepsApi
import com.carecrafter.models.User
import com.carecrafter.models.WaterApi
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Achievements : Fragment() {
    private lateinit var binding: AchievementsHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var progress = 0

    //Increment Checker
    var hasIncrementedWater1 = false
    var hasIncrementedWater2 = false
    var hasIncrementedWater3 = false
    var hasIncrementedWater4 = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AchievementsHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getTotalSteps(it) }
        authToken.toString().let { getTotalSleeps(it) }
        authToken.toString().let { getTotalWater(it) }
        binding.ivBack.setOnClickListener {
            findNavController().navigate(AchievementsDirections.actionAchievementsToAccountFragment())
        }

        //Descriptions Water Intake
        binding.achievementWater1.setOnClickListener {
            if (!hasIncrementedWater1) {
                binding.description.text = "Drink at least one time"
                hasIncrementedWater1 = true
            }
        }
        binding.achievementWater2.setOnClickListener {
            if (!hasIncrementedWater2) { // Add this check
                binding.description.text = "Drink at least 10,000ml"
                hasIncrementedWater2 = true
            }
        }
        binding.achievementWater3.setOnClickListener{
            if (!hasIncrementedWater3) { // Add this check
                binding.description.text = "Drink at least 100,000ml"
                hasIncrementedWater3 = true
            }
        }
        binding.achievementWater4.setOnClickListener{
            if (!hasIncrementedWater4) { // Add this check
                binding.description.text = "Drink at least 1,000,000ml"
                hasIncrementedWater4 = true
            }
        }

        //Descriptions Step Tracker
        binding.achievementStep1.setOnClickListener{
            binding.description.text = "Walk at least 100 steps"
        }
        binding.achievementStep2.setOnClickListener{
            binding.description.text = "Walk at least 1,000 steps"
        }
        binding.achievementStep3.setOnClickListener{
            binding.description.text = "Walk at least 10,000 steps"
        }
        binding.achievementStep4.setOnClickListener{
            binding.description.text = "Walk at least 100,000 steps"
        }

        //Descriptions Sleep Tracker
        binding.achievementSleep1.setOnClickListener{
            binding.description.text = "Sleep for a total of 1 hr"
        }
        binding.achievementSleep2.setOnClickListener{
            binding.description.text = "Sleep for a total of 8 hr"
        }
        binding.achievementSleep3.setOnClickListener{
            binding.description.text = "Sleep for a total of 24 hrs"
        }
        binding.achievementSleep4.setOnClickListener{
            binding.description.text = "Sleep for a total of 168 hrs"
        }

        return binding.root
    }

    private fun updatebar(){
        binding.progressbar.progress = progress
    }

    private fun getTotalSteps(authToken: String) {
        ApiClient.instance.totalSteps("Bearer $authToken").enqueue(object : Callback<StepsApi> {
            override fun onResponse(call: Call<StepsApi>, response: Response<StepsApi>) {
                if (response.isSuccessful) {
                    val stepData = response.body()
                    if (stepData != null) {
                        updateInfo(stepData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get step info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StepsApi>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get step info", t)
                Toast.makeText(requireContext(), "Failed to step user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateInfo(stepData: StepsApi) {
        if (stepData != null) {
            if (stepData.totalSteps.toInt() >= 100){
                binding.achievementStep1.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (stepData.totalSteps.toInt() >= 1000){
                binding.achievementStep1.setImageTintList(null)
                binding.achievementStep2.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (stepData.totalSteps.toInt() >= 10000){
                binding.achievementStep1.setImageTintList(null)
                binding.achievementStep2.setImageTintList(null)
                binding.achievementStep3.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (stepData.totalSteps.toInt() >= 100000){
                binding.achievementStep1.setImageTintList(null)
                binding.achievementStep2.setImageTintList(null)
                binding.achievementStep3.setImageTintList(null)
                binding.achievementStep4.setImageTintList(null)
                progress += 10
                updatebar()
            }
        }
        if (progress == 10){
            binding.tvProgress.text = "1 out of 12"
        }
        if (progress == 20){
            binding.tvProgress.text = "2 out of 12"
        }
        if (progress == 30){
            binding.tvProgress.text = "3 out of 12"
        }
        if (progress == 40){
            binding.tvProgress.text = "4 out of 12"
        }
        if (progress == 50){
            binding.tvProgress.text = "5 out of 12"
        }
        if (progress == 60){
            binding.tvProgress.text = "6 out of 12"
        }
        if (progress == 70){
            binding.tvProgress.text = "7 out of 12"
        }
        if (progress == 80){
            binding.tvProgress.text = "8 out of 12"
        }
        if (progress == 90){
            binding.tvProgress.text = "9 out of 12"
        }
        if (progress == 100){
            binding.tvProgress.text = "10 out of 12"
        }
        if (progress == 110){
            binding.tvProgress.text = "11 out of 12"
        }
        if (progress == 120){
            binding.tvProgress.text = "12 out of 12"
        }
    }

    private fun getTotalSleeps(authToken: String) {
        ApiClient.instance.totalSleeps("Bearer $authToken").enqueue(object : Callback<SleepsApi> {
            override fun onResponse(call: Call<SleepsApi>, response: Response<SleepsApi>) {
                if (response.isSuccessful) {
                    val sleepData = response.body()
                    if (sleepData != null) {
                        updateSleepInfo(sleepData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get Sleep info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SleepsApi>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get Sleep info", t)
                Toast.makeText(requireContext(), "Failed to Sleep info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSleepInfo(sleepData: SleepsApi) {
        if (sleepData != null) {
            if (sleepData.totalSleeps >= 100){
                binding.achievementSleep1.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (sleepData.totalSleeps >= 1000){
                binding.achievementSleep1.setImageTintList(null)
                binding.achievementSleep2.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (sleepData.totalSleeps >= 10000){
                binding.achievementSleep1.setImageTintList(null)
                binding.achievementSleep2.setImageTintList(null)
                binding.achievementSleep3.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (sleepData.totalSleeps >= 100000){
                binding.achievementSleep1.setImageTintList(null)
                binding.achievementSleep2.setImageTintList(null)
                binding.achievementSleep3.setImageTintList(null)
                binding.achievementSleep4.setImageTintList(null)
                progress += 10
                updatebar()
            }
        }
        if (progress == 10){
            binding.tvProgress.text = "1 out of 12"
        }
        if (progress == 20){
            binding.tvProgress.text = "2 out of 12"
        }
        if (progress == 30){
            binding.tvProgress.text = "3 out of 12"
        }
        if (progress == 40){
            binding.tvProgress.text = "4 out of 12"
        }
        if (progress == 50){
            binding.tvProgress.text = "5 out of 12"
        }
        if (progress == 60){
            binding.tvProgress.text = "6 out of 12"
        }
        if (progress == 70){
            binding.tvProgress.text = "7 out of 12"
        }
        if (progress == 80){
            binding.tvProgress.text = "8 out of 12"
        }
        if (progress == 90){
            binding.tvProgress.text = "9 out of 12"
        }
        if (progress == 100){
            binding.tvProgress.text = "10 out of 12"
        }
        if (progress == 110){
            binding.tvProgress.text = "11 out of 12"
        }
        if (progress == 120){
            binding.tvProgress.text = "12 out of 12"
        }
    }

    private fun getTotalWater(authToken: String) {
        ApiClient.instance.totalWater("Bearer $authToken").enqueue(object : Callback<WaterApi> {
            override fun onResponse(call: Call<WaterApi>, response: Response<WaterApi>) {
                if (response.isSuccessful) {
                    val waterData = response.body()
                    if (waterData != null) {
                        updateWaterInfo(waterData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get Water info", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WaterApi>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get Water info", t)
                Toast.makeText(requireContext(), "Failed to Water info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateWaterInfo(waterData: WaterApi) {
        if (waterData != null) {
            if (waterData.totalWater.toInt() >= 100){
                binding.achievementWater1.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (waterData.totalWater.toInt()  >= 1000){
                binding.achievementWater1.setImageTintList(null)
                binding.achievementWater2.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (waterData.totalWater.toInt()  >= 10000){
                binding.achievementWater1.setImageTintList(null)
                binding.achievementWater2.setImageTintList(null)
                binding.achievementWater3.setImageTintList(null)
                progress += 10
                updatebar()
            }
            if (waterData.totalWater.toInt()  >= 100000){
                binding.achievementWater1.setImageTintList(null)
                binding.achievementWater2.setImageTintList(null)
                binding.achievementWater3.setImageTintList(null)
                binding.achievementWater4.setImageTintList(null)
                progress += 10
                updatebar()
            }
        }
        if (progress == 10){
            binding.tvProgress.text = "1 out of 12"
        }
        if (progress == 20){
            binding.tvProgress.text = "2 out of 12"
        }
        if (progress == 30){
            binding.tvProgress.text = "3 out of 12"
        }
        if (progress == 40){
            binding.tvProgress.text = "4 out of 12"
        }
        if (progress == 50){
            binding.tvProgress.text = "5 out of 12"
        }
        if (progress == 60){
            binding.tvProgress.text = "6 out of 12"
        }
        if (progress == 70){
            binding.tvProgress.text = "7 out of 12"
        }
        if (progress == 80){
            binding.tvProgress.text = "8 out of 12"
        }
        if (progress == 90){
            binding.tvProgress.text = "9 out of 12"
        }
        if (progress == 100){
            binding.tvProgress.text = "10 out of 12"
        }
        if (progress == 110){
            binding.tvProgress.text = "11 out of 12"
        }
        if (progress == 120){
            binding.tvProgress.text = "12 out of 12"
        }
    }

}