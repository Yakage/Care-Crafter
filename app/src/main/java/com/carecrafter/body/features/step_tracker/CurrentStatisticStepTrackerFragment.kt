package com.carecrafter.body.features.step_tracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.carecrafter.R
import com.carecrafter.body.adapters.StepHistoryAdapter
import com.carecrafter.databinding.StepTrackerCurrentStatisticBinding
import com.carecrafter.models.StepHistory
import com.carecrafter.models.StepHistoryApi
import com.carecrafter.models.StepsMonthlyStatsApi
import com.carecrafter.models.StepsWeeklyStatsApi
import com.carecrafter.retrofit_database.ApiClient
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentStatisticStepTrackerFragment : Fragment() {
    private lateinit var binding: StepTrackerCurrentStatisticBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var barChart: BarChart
    private lateinit var stepHistoryAdapter: StepHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StepTrackerCurrentStatisticBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        setupViews(authToken.toString())
        stepHistoryAdapter = StepHistoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = stepHistoryAdapter
        getStepHistory(authToken.toString())
        binding.ivBack.setOnClickListener {
            findNavController().navigate(R.id.action_currentStatisticStepTrackerFragment_to_currentUpdatingStepTrackerFragment)
        }

        return binding.root
    }
    private fun setupViews(authToken: String) {
        barChart = binding.barChart

        setupSpinner(authToken)

        // Set up chart
        setupChart()
    }

    private fun setupSpinner(authToken: String) {
        val spinnerItems = listOf("Weekly", "Monthly")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTimeFrame.adapter = adapter

        binding.spinnerTimeFrame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showWeeklyData(authToken)
                    1 -> showMonthlyData(authToken)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupChart() {
        barChart.setNoDataText("No data available")
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.setDrawBarShadow(false)
        barChart.axisRight.isEnabled = false
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
    }

    private fun showWeeklyData(authToken: String) {
        ApiClient.instance.chartDataStepsWeekly("Bearer $authToken").enqueue(object :
            Callback<List<StepsWeeklyStatsApi>> {
            override fun onResponse(
                call: Call<List<StepsWeeklyStatsApi>>,
                response: Response<List<StepsWeeklyStatsApi>>
            ) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()
                    val data = response.body()

                    data?.forEachIndexed { index, stepsApi ->
                        // Add the total steps for each week as BarEntry
                        entries.add(BarEntry(index.toFloat(), stepsApi.value.toFloat()))
                        // Use the first three letters of the day name as label
                        labels.add(stepsApi.label.substring(0, 3))
                    }
                    setData(entries, labels)
                } else {
                    Toast.makeText(requireContext(), "Failed to get weekly steps data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepsWeeklyStatsApi>>, t: Throwable) {
                Log.e("StepsStatisticFragment", "Failed to get weekly steps data", t)
                Toast.makeText(requireContext(), "Failed to get weekly steps data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showMonthlyData(authToken: String) {
        ApiClient.instance.chartDataStepsMonthly("Bearer $authToken").enqueue(object :
            Callback<List<StepsMonthlyStatsApi>> {
            override fun onResponse(
                call: Call<List<StepsMonthlyStatsApi>>,
                response: Response<List<StepsMonthlyStatsApi>>
            ) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()

                    response.body()?.forEachIndexed { index, stepsApi ->
                        // Add the total steps for each week as BarEntry
                        entries.add(BarEntry(index.toFloat(), stepsApi.steps.toFloat()))
                        // Use the "week" field as label
                        labels.add(stepsApi.week)
                    }

                    setData(entries, labels)
                } else {
                    Toast.makeText(requireContext(), "Failed to get monthly steps data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepsMonthlyStatsApi>>, t: Throwable) {
                Log.e("StatisticStepTrackerFragment", "Failed to get monthly steps data", t)
                Toast.makeText(requireContext(), "Failed to get monthly steps data", Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun setData(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Steps")
        dataSet.color = requireContext().getColor(R.color.blue)

        val data = BarData(dataSet)
        data.barWidth = 0.5f
        barChart.data = data

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }


    private fun getStepHistory(authToken: String) {
        ApiClient.instance.getStepHistory2("Bearer $authToken").enqueue(object :
            Callback<List<StepHistoryApi>> {
            override fun onResponse(call: Call<List<StepHistoryApi>>, response: Response<List<StepHistoryApi>>) {
                if (response.isSuccessful) {
                    val stepHistoryApis = response.body() ?: emptyList()
                    val entries = stepHistoryApis.map { stepHistoryApi ->
                        StepHistory(
                            created_at = stepHistoryApi.date,
                            current_steps = stepHistoryApi.current_steps,
                            daily_goal = stepHistoryApi.daily_goal
                        )
                    }
                    stepHistoryAdapter.updateData(entries)
                } else {
                    Toast.makeText(requireContext(), "Failed to get step history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<StepHistoryApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get step history", t)
                Toast.makeText(requireContext(), "Failed to get step history", Toast.LENGTH_SHORT).show()
            }
        })
    }
}