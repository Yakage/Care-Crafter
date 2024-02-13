package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerSettingBinding
import com.carecrafter.databinding.SleepTrackerStatisticsBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        return binding.root
    }
}