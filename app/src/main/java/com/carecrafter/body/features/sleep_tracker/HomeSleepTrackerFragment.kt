package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerHomeBinding

class HomeSleepTrackerFragment : Fragment() {

    private lateinit var binding: SleepTrackerHomeBinding
    private lateinit var ivAlarm: ImageView
    private lateinit var btStop: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)

        binding.ivAlarm.setOnClickListener{
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_settingsSleepTrackerFragment)
        }
        binding.btStop.setOnClickListener{
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_statisticSleepTrackerFragment)
        }
        return binding.root
    }
}