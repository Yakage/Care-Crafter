package com.carecrafter.body.features.water_intake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carecrafter.R
import com.carecrafter.body.adapters.WaterHistoryAdapter
import com.carecrafter.databinding.ActivityStatisticsWaterIntakeBinding
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


class StatisticsWaterIntakeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsWaterIntakeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var barChart: BarChart
    private lateinit var waterHistoryAdapter: WaterHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsWaterIntakeBinding.inflate(layoutInflater)
        sharedPreferences = this.getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        setupViews(authToken.toString())

        waterHistoryAdapter = WaterHistoryAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this@StatisticsWaterIntakeActivity)
        binding.recyclerView.adapter = waterHistoryAdapter
        getWaterHistory(authToken.toString())
        binding.ivBack.setOnClickListener {
            val intent = Intent(this, WaterIntakeBActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)

    }

    private fun setupViews(authToken: String) {
        barChart = binding.barChart

        setupSpinner(authToken)

        // Set up chart
        setupChart()
    }

    private fun setupSpinner(authToken: String) {
        val spinnerItems = listOf("Weekly", "Monthly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
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

    private fun setData(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Steps")
        dataSet.color = this.getColor(R.color.blue)

        val data = BarData(dataSet)
        data.barWidth = 0.5f
        barChart.data = data

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.invalidate()
    }

    private fun showWeeklyData(authToken: String) {
        ApiClient.instance.chartDataWaterWeekly("Bearer $authToken").enqueue(object :
            Callback<List<WaterWeeklyStatsApi>> {
            override fun onResponse(call: Call<List<WaterWeeklyStatsApi>>, response: Response<List<WaterWeeklyStatsApi>>) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()
                    val data = response.body()

                    data?.forEach { waterApi ->
                        entries.add(BarEntry(entries.size.toFloat(), waterApi.value.toFloat()))
                        // Use the first three letters of the day name as label
                        val dayName = waterApi.label.substring(0, 3)
                        labels.add(dayName)
                    }
                    setData(entries, labels)
                } else {
                }
            }

            override fun onFailure(call: Call<List<WaterWeeklyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get weekly water data", t)
            }
        })
    }


    private fun showMonthlyData(authToken: String) {
        ApiClient.instance.chartDataWaterMonthly("Bearer $authToken").enqueue(object :
            Callback<List<WaterMonthlyStatsApi>> {
            override fun onResponse(
                call: Call<List<WaterMonthlyStatsApi>>,
                response: Response<List<WaterMonthlyStatsApi>>
            ) {
                if (response.isSuccessful) {
                    val entries = mutableListOf<BarEntry>()
                    val labels = mutableListOf<String>()
                    val data = response.body()

                    data?.forEachIndexed { index, waterApi ->
                        entries.add(BarEntry(index.toFloat(), waterApi.water.toFloat()))
                        // Use the first three letters of the week name as label
                        labels.add(waterApi.week.substring(0, 3))
                    }

                    setData(entries, labels)
                } else {
                }
            }

            override fun onFailure(call: Call<List<WaterMonthlyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get monthly water data", t)
            }
        })
    }



    private fun getWaterHistory(authToken: String) {
        ApiClient.instance.getWaterHistory2("Bearer $authToken").enqueue(object : Callback<List<WaterHistoryApi>> {
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
                }
            }

            override fun onFailure(call: Call<List<WaterHistoryApi>>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get step history", t)
            }
        })
    }
}