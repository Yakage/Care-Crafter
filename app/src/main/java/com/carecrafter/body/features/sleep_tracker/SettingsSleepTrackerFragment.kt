package com.carecrafter.body.features.sleep_tracker

import android.content.Context
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
import com.carecrafter.databinding.SleepTrackerSettingBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader


class SettingsSleepTrackerFragment : Fragment() {
    private lateinit var binding: SleepTrackerSettingBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerSettingBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")

        binding.ivBack.setOnClickListener{
            findNavController().navigate(R.id.action_settingsSleepTrackerFragment_to_homeSleepTrackerFragment)
        }
        binding.submitButton.setOnClickListener {
            createAlarm(authToken.toString())
        }
        return binding.root
    }

    private fun createAlarm(authToken: String){
        val title = binding.alarmTitle.text.toString().trim()
        val message = binding.alarmMessage.text.toString().trim()
        val hour: Int = binding.timePicker.currentHour
        val minute: Int = binding.timePicker.currentMinute
        val time = "$hour:$minute"
        val day: Int = binding.datePicker.dayOfMonth
        val month: Int = binding.datePicker.month + 1
        val year: Int = binding.datePicker.year
        val date = "$year:$month:$day"

        val createAlarmDataJson =
            "{\"authToken\":\"$authToken\"\"title\":\"$title\",\"message\":\"$message\",\"time\":\"$time\",\"date\":\"$date\"}"

        //validation
        if (title.isEmpty()) {
            binding.alarmTitle.error = "Title required"
            binding.alarmTitle.requestFocus()
        }

        if (message.toString().isEmpty()) {
            binding.alarmMessage.error = "Message required"
            binding.alarmMessage.requestFocus()
        }

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createAlarmDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createAlarm(
                "Bearer $authToken",
                title,
                message,

            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(SettingsSleepTrackerFragmentDirections.actionSettingsSleepTrackerFragmentToHomeSleepTrackerFragment())
                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Toast.makeText(
                                requireContext(),
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
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