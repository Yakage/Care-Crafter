package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.SleepTrackerSetGoalBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class GoalSetSleepTrackerFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: SleepTrackerSetGoalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SleepTrackerSetGoalBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        binding.nextBtn.setOnClickListener {
            createAlarm(authToken.toString())
        }
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
    private fun createAlarm(authToken: String){
        val dailyGoal = binding.etTimeGoal.text.toString().trim()
        val time = "0:0"
        val createAlarmDataJson =
            "{\"authToken\":\"$authToken\",\"dailyGoal\":\"$dailyGoal\",\"time\":\"$time\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createAlarmDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createAlarm(
                "Bearer $authToken",
                dailyGoal,
                time
            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            findNavController().navigate(R.id.action_goalSetSleepTrackerFragment_to_sleepTrackingFragment)
                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

}