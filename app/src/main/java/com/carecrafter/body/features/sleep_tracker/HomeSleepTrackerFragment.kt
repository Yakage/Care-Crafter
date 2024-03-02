package com.carecrafter.body.features.sleep_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.carecrafter.databinding.SleepTrackerHomeBinding

class HomeSleepTrackerFragment : Fragment() {

    private lateinit var binding: SleepTrackerHomeBinding
    private var isRunning = false
    private var timerSeconds = 0

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var sharedPreferences: SharedPreferences
    private val runnable = object : Runnable {
        override fun run() {
            timerSeconds++
            val hours = timerSeconds / 3600
            val minutes = (timerSeconds % 3600) / 60
            val seconds = timerSeconds % 60

            val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
//            binding.timeTV.text = time

            handler.postDelayed(this, 1000)
        }
    }
    private val scoreHistoryList = mutableListOf<Pair<Int, String>>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)

//        val authToken = sharedPreferences.getString("authToken", "")
//        authToken?.let { getAlarmInfo(it) }
//        binding.ivAlarm.setOnClickListener{
//            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_settingsSleepTrackerFragment)
//        }
//        binding.statsBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_statisticSleepTrackerFragment)
//        }
//        binding.startBtn.setOnClickListener{
//            startTimer()
//        }
//        binding.stopBtn.setOnClickListener{
//            stopTimer()
//        }
//        binding.resetBtn.setOnClickListener{
//            resetTimer()
//        }
//        binding.rateBtn.setOnClickListener {
//            calculator(authToken.toString())
//        }

        // Date and Time for the TextView
//        val calendar = Calendar.getInstance().time
//        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG).format(calendar)
//        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)
//        binding.currentTime.text = "$timeFormat"
//        binding.currentDate.text = "$dateFormat"

//        binding.ivBack.setOnClickListener {
//            val intent = Intent(activity, BodyActivity::class.java)
//            startActivity(intent)
//        }
        return binding.root
    }
//    private fun startTimer(){
//        if (!isRunning){
//            handler.postDelayed(runnable, 1000)
//            isRunning = true
//
//            binding.startBtn.isEnabled = false
//            binding.stopBtn.isEnabled = true
//            binding.resetBtn.isEnabled = true
//            binding.rateBtn.isEnabled = false
//            binding.statsBtn.isEnabled = false
//        }
//    }
//    private fun stopTimer(){
//        if (isRunning){
//            handler.removeCallbacks(runnable)
//            isRunning = false
//
//            binding.startBtn.isEnabled = true
//            binding.startBtn.text = "Resume"
//            binding.stopBtn.isEnabled = false
//            binding.resetBtn.isEnabled = true
//            binding.rateBtn.isEnabled = true
//            binding.statsBtn.isEnabled = false
//        }
//    }
//    private fun resetTimer(){
//        stopTimer()
//
//        timerSeconds = 0
//        binding.timeTV.text = "00:00:00"
//
//        binding.startBtn.isEnabled = true
//        binding.stopBtn.isEnabled = false
//        binding.startBtn.text = "Start"
//        binding.resetBtn.isEnabled = false
//        binding.rateBtn.isEnabled = false
//        binding.statsBtn.isEnabled = false
//    }
//
//    private fun calculator(authToken: String) {
//        binding.startBtn.isEnabled = true
//        binding.stopBtn.isEnabled = false
//        binding.resetBtn.isEnabled = false
//        binding.rateBtn.isEnabled = false
//        binding.statsBtn.isEnabled = true
//        // The 28800 is Seconds if which converted its 8 Hours
//        val rate = (timerSeconds / 28800) * 100
//        val hours = timerSeconds / 3600
//        val minutes = (timerSeconds % 3600) / 60
//
//        // I use if statement to prevent the score going over 100
//        if (rate >= 100) {
//            val rate = 100
//            val currentTime = Calendar.getInstance().time
//            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
//            scoreHistoryList.add(Pair(rate, timeString))
//
//            var scoreHistory = "Sleep Score History:\n"
//            for (score in scoreHistoryList) {
//                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes hrs of sleep at ${score.second}\n"
//            }
//            val scoreData = "Score: $rate Time: $timeString"
//            createScore(authToken, scoreData)
//            binding.scoreLogs.text = scoreHistory.toString()
//        }
//
//        else {
//            val currentTime = Calendar.getInstance().time
//            val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
//            scoreHistoryList.add(Pair(rate, timeString))
//
//            var scoreHistory = "Sleep Score History:\n"
//            for (score in scoreHistoryList) {
//                scoreHistory += " - ${score.first} over 100 with a total of $hours.$minutes hrs of sleep at ${score.second}\n"
//            }
//            val scoreData = "Score: $rate Time: $timeString"
//            createScore(authToken, scoreData)
//            binding.scoreLogs.text = scoreHistory.toString()
//
//        }
//    }
//    private fun createScore(authToken: String, scoreHistory: String ){
//
//        val createScoreDataJson =
//            "{\"authToken\":\"$authToken\"\"score\":\"$scoreHistory\"}"
//
//
//        //correct malformed data
//        try {
//            val reader = JsonReader(StringReader(createScoreDataJson))
//            reader.isLenient = true
//            reader.beginObject()
//            reader.close()
//            ApiClient.instance.createScore(
//                "Bearer $authToken",
//                scoreHistory
//            )
//                .enqueue(object : Callback<DefaultResponse> {
//                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
//                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG)
//                            .show()
//                    }
//
//                    override fun onResponse(
//                        call: Call<DefaultResponse>,
//                        response: Response<DefaultResponse>
//                    ) {
//                        if (response.isSuccessful && response.body() != null) {
//                            Toast.makeText(
//                                requireContext(),
//                                response.body()!!.message,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        } else {
//                            val errorMessage: String = try {
//                                response.errorBody()?.string()
//                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
//                            } catch (e: Exception) {
//                                "Failed to get a valid response. Response code: ${response.code()}"
//                            }
//                            Toast.makeText(
//                                requireContext(),
//                                errorMessage,
//                                Toast.LENGTH_LONG
//                            )
//                                .show()
//                            Log.e("API_RESPONSE", errorMessage)
//                        }
//                    }
//                })
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT)
//                .show()
//            e.printStackTrace()
//        }
//    }
//
//    private fun getAlarmInfo(authToken: String) {
//        ApiClient.instance.getAlarm("Bearer $authToken").enqueue(object : Callback<Alarm>{
//            override fun onResponse(call: Call<Alarm>, response: Response<Alarm>) {
//                if (response.isSuccessful) {
//                    val alarmData = response.body()
//                    if (alarmData != null) {
//                        updateInfo(alarmData)
//                    }
//
//                    val responseBody = response.body().toString()
//                    Log.d("Response", responseBody)
//                } else {
//                    // Handle unsuccessful response
//                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<Alarm>, t: Throwable) {
//                Log.e("AccountFragment", "Failed to get user info", t)
//                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    fun updateInfo(alarmData: Alarm){
//        if (alarmData != null) {
//            binding.textView3.text = alarmData.time
//        }
//    }
}
