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
import com.carecrafter.databinding.SleepTrackerResultBinding
import com.carecrafter.models.Alarm
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.SleepScoreLogs
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import java.text.DateFormat
import java.util.Calendar


class ResultSleepTrackerFragment : Fragment() {
    private lateinit var binding: SleepTrackerResultBinding
    private lateinit var sharedPreferences: SharedPreferences
    var input = 0
    var scorefinal = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SleepTrackerResultBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken?.let { getAlarmInfo(it) }
        authToken?.let { getScore(it) }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_resultSleepTrackerFragment_to_homeSleepTrackerFragment)
            requireActivity().finish()
        }

        // Date for the TextView
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
        binding.tvTime.text = "$dateFormat"

        return binding.root
    }

    private fun testing(authToken: String){
        val seconds = binding.tvTimerSeconds.text.toString().toInt().toDouble()
        val input = input.toDouble()
        val scoreDivide = (input * 60 * 60).toInt()
        val minute = (seconds / 60).toInt()
        val hour = (minute / 60).toInt()
        val score = ((seconds / input) * 100).toInt() // this line is to test if it works
        //val score = ((seconds / scoreDivide) * 100).toInt()
        if (score >= 100) {
            scorefinal = 100
        } else {
            scorefinal = score
        }
        binding.tvTotalTime.text = "$hour.$minute hrs\n$scorefinal/100"

        binding.progressBar.progress = score
        createSleep(authToken, scorefinal.toString(), seconds.toInt())
        if (scorefinal == 100) {
            binding.tvComplete.visibility = View.VISIBLE
            binding.tvAlmost.visibility = View.GONE
            binding.tvHalf.visibility = View.GONE
            binding.tvBarely.visibility = View.GONE
        } else if (scorefinal in 75..99) {
            binding.tvComplete.visibility = View.GONE
            binding.tvAlmost.visibility = View.VISIBLE
            binding.tvHalf.visibility = View.GONE
            binding.tvBarely.visibility = View.GONE
        } else if (scorefinal in 50..74) {
            binding.tvComplete.visibility = View.GONE
            binding.tvAlmost.visibility = View.GONE
            binding.tvHalf.visibility = View.VISIBLE
            binding.tvBarely.visibility = View.GONE
        } else if (scorefinal in 0..49) {
            binding.tvComplete.visibility = View.GONE
            binding.tvAlmost.visibility = View.GONE
            binding.tvHalf.visibility = View.GONE
            binding.tvBarely.visibility = View.VISIBLE
        }
    }
    private fun getAlarmInfo(authToken: String) {
        ApiClient.instance.getAlarm("Bearer $authToken").enqueue(object : Callback<Alarm>{
            override fun onResponse(call: Call<Alarm>, response: Response<Alarm>) {
                if (response.isSuccessful) {
                    val alarmData = response.body()
                    if (alarmData != null) {
                        updateInfo(alarmData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Alarm>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateInfo(alarmData: Alarm){
        if (alarmData != null) {
            input = alarmData.daily_goal.toInt()
        }
    }
    private fun getScore(authToken: String) {
        ApiClient.instance.getScore("Bearer $authToken").enqueue(object : Callback<SleepScoreLogs> {
            override fun onResponse(call: Call<SleepScoreLogs>, response: Response<SleepScoreLogs>) {
                if (response.isSuccessful) {
                    val scoreData = response.body()
                    if (scoreData != null) {
                        updateScore(scoreData)
                    }
                    testing(authToken)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SleepScoreLogs>, t: Throwable) {
                Log.e("SleepTracker", "Failed to get logs info", t)
                Toast.makeText(requireContext(), "Failed to get logs info", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateScore(scoreData: SleepScoreLogs){
        binding.tvTimerSeconds.text = scoreData.total_time
    }

    private fun createSleep(authToken: String, score: String, sleeps: Int){
        val createSleepDataJson =
            "{\"authToken\":\"$authToken\",\"score\":\"$score\",\"sleeps\":\"$sleeps\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createSleepDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createSleep(
                "Bearer $authToken",
                score,
                sleeps
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