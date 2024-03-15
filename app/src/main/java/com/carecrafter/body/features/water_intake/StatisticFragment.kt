package com.carecrafter.body.features.water_intake

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.WaterIntakeStatisticBinding
import com.carecrafter.models.WaterDailyStatsApi
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

class StatisticFragment : Fragment() {
    private lateinit var binding: WaterIntakeStatisticBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = WaterIntakeStatisticBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        setupViews(authToken.toString())

        binding.ivBack.setOnClickListener {
            findNavController().navigate(StatisticFragmentDirections.actionStatisticFragment2ToHomeFragment())
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

    private fun setData(entries: List<BarEntry>, labels: List<String>) {
        val dataSet = BarDataSet(entries, "Steps")
        dataSet.color = requireContext().getColor(R.color.blue)

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
                    Toast.makeText(requireContext(), "Failed to get monthly water data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WaterMonthlyStatsApi>>, t: Throwable) {
                Log.e("StatisticWaterIntakeFragment", "Failed to get monthly water data", t)
                Toast.makeText(requireContext(), "Failed to get monthly water data", Toast.LENGTH_SHORT).show()
            }
        })
    }


}