package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerSettingBinding

class SettingsSleepTrackerFragment : Fragment() {
    private lateinit var binding: SleepTrackerSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerSettingBinding.inflate(inflater, container, false)

//        binding.tvConfirm.setOnClickListener{
//            findNavController().navigate(R.id.action_settingsSleepTrackerFragment_to_homeSleepTrackerFragment)
//            Toast.makeText(requireActivity(), "Dapat Data updated kaso wala pa (☉Д☉)!", Toast.LENGTH_SHORT).show()
//        }
        binding.ivBack.setOnClickListener{
            findNavController().navigate(R.id.action_settingsSleepTrackerFragment_to_homeSleepTrackerFragment)
        }
        return binding.root
    }
}