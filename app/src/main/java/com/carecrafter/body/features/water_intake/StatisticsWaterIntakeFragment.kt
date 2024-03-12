package com.carecrafter.body.features.water_intake

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.carecrafter.R
import com.carecrafter.body.adapters.WaterHistoryAdapter
import com.carecrafter.databinding.WaterIntakeStatisticBinding
import com.carecrafter.models.WaterDailyStatsApi
import com.carecrafter.models.WaterHistory
import com.carecrafter.models.WaterHistoryApi
import com.carecrafter.models.WaterMonthlyStatsApi
import com.carecrafter.models.WaterWeeklyStatsApi
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


class StatisticsWaterIntakeFragment : Fragment() {

    private lateinit var binding: WaterIntakeStatisticBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var barChart: BarChart
    private lateinit var waterHistoryAdapter: WaterHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WaterIntakeStatisticBinding.inflate(inflater, container, false)

        sharedPreferences = requireContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        setupViews(authToken.toString())

        waterHistoryAdapter = WaterHistoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = waterHistoryAdapter
        getWaterHistory(authToken.toString())
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireContext(), WaterIntakeBActivity::class.java)
            startActivity(intent)
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
        val spinnerItems = listOf("Daily", "Weekly", "Monthly")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTimeFrame.adapter = adapter

        binding.spinnerTimeFrame.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> showDailyData(authToken)
                    1 -> showWeeklyData(authToken)
                    2 -> showMonthlyData(authToken)
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

    private fun setData(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Steps")
        dataSet.color = requireContext().getColor(R.color.blue)

        val data = BarData(dataSet)
        data.barWidth = 0.5f
        barChart.data = data

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }

    private fun showDailyData(authToken: String) {
        ApiClient.instance.getDailyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterDailyStatsApi>> {
            override fun onResponse(call: Call<List<WaterDailyStatsApi>>, response: Response<List<WaterDailyStatsApi>>) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()

                    // Initialize water intake map for each day of the week with zero intake
                    val waterByDay = mutableMapOf<Int, Float>().apply {
                        for (i in 0..6) {
                            put(i, 0f)
                        }
                    }

                    // Update water intake for each day based on the response data
                    response.body()?.forEach { waterApi ->
                        val dayOfWeek = waterApi.dayOfWeek
                        val totalWater = waterApi.totalWater.toFloat()
                        waterByDay[dayOfWeek] = totalWater
                    }

                    // Populate entries and labels with water intake for each day
                    for (i in 0..6) {
                        val dayLabel = when (i) {
                            0 -> "Sun"
                            1 -> "Mon"
                            2 -> "Tue"
                            3 -> "Wed"
                            4 -> "Thu"
                            5 -> "Fri"
                            6 -> "Sat"
                            else -> ""
                        }
                        labels.add(dayLabel)
                        entries.add(BarEntry(i.toFloat(), waterByDay[i] ?: 0f))
                    }

                    setData(entries, labels)
                } else {
                    Toast.makeText(requireContext(), "Failed to get daily water data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WaterDailyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get daily water data", t)
                Toast.makeText(requireContext(), "Failed to get daily water data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showWeeklyData(authToken: String) {
        ApiClient.instance.getWeeklyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterWeeklyStatsApi>> {
            override fun onResponse(call: Call<List<WaterWeeklyStatsApi>>, response: Response<List<WaterWeeklyStatsApi>>) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()
                    val data = response.body()

                    data?.forEach { waterApi ->
                        entries.add(BarEntry(entries.size.toFloat(), waterApi.totalWater.toFloat()))
                        labels.add(waterApi.weekStartDate) // Use week start date as label
                    }
                    setData(entries, labels)
                } else {
                    Toast.makeText(requireContext(), "Failed to get weekly water data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WaterWeeklyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get weekly water data", t)
                Toast.makeText(requireContext(), "Failed to get weekly water data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMonthlyData(authToken: String) {
        ApiClient.instance.getMonthlyWater("Bearer $authToken").enqueue(object :
            Callback<List<WaterMonthlyStatsApi>> {
            override fun onResponse(
                call: Call<List<WaterMonthlyStatsApi>>,
                response: Response<List<WaterMonthlyStatsApi>>
            ) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()

                    // Initialize water intake map for each month with zero intake
                    val waterByMonth = mutableMapOf<Int, Float>().apply {
                        for (i in 0..11) {
                            put(i, 0f)
                        }
                    }

                    // Update water intake for each month based on the response data
                    response.body()?.forEach { waterApi ->
                        val monthNumber = waterApi.monthNumber
                        val totalWater = waterApi.totalWater.toFloat()
                        waterByMonth[monthNumber] = totalWater
                    }

                    // Populate entries and labels with water intake for each month
                    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    for (i in 0..11) {
                        val monthLabel = months[i]
                        labels.add(monthLabel)
                        entries.add(BarEntry(i.toFloat(), waterByMonth[i] ?: 0f))
                    }

                    setData(entries, labels)
                } else {
                    Toast.makeText(requireContext(), "Failed to get monthly water data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WaterMonthlyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get monthly water data", t)
                Toast.makeText(requireContext(), "Failed to get monthly water data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getWaterHistory(authToken: String) {
        ApiClient.instance.getWaterHistory2("Bearer $authToken").enqueue(object :
            Callback<List<WaterHistoryApi>> {
            override fun onResponse(call: Call<List<WaterHistoryApi>>, response: Response<List<WaterHistoryApi>>) {
                if (response.isSuccessful) {
                    val waterHistoryApi = response.body() ?: emptyList()
                    val entries = waterHistoryApi.map { waterHistoryApi ->
                        WaterHistory(
                            created_at = waterHistoryApi.date,
                            daily_goal = waterHistoryApi.daily_goal,
                            current_water = waterHistoryApi.currentWater,
                            history = waterHistoryApi.history,
                        )
                    }
                    waterHistoryAdapter.updateData(entries)
                } else {
                    Toast.makeText(requireContext(), "Failed to get water history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WaterHistoryApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get step history", t)
                Toast.makeText(requireContext(), "Failed to get water history", Toast.LENGTH_SHORT).show()
            }
        })
    }

}