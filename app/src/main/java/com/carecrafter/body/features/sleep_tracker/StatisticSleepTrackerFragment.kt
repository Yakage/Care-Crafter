package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerStatisticsBinding
import java.text.DateFormat
import java.util.Calendar

class StatisticSleepTrackerFragment : Fragment() {
    private lateinit var binding: SleepTrackerStatisticsBinding
    private lateinit var ivBack: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerStatisticsBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener{
            findNavController().navigate(R.id.action_statisticSleepTrackerFragment_to_homeSleepTrackerFragment)
        }

        val calendar = Calendar.getInstance().time
//      val dateFormat = DateFormat.getDateInstance(DateFormat.LONG).format(calendar)
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)
        binding.currentTime.text = "$timeFormat"

        return binding.root
    }
}