package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.databinding.SleepTrackerHomeBinding
import com.carecrafter.databinding.SleepTrackerSettingBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsSleepTrackerFragment : Fragment() {
    private lateinit var binding: SleepTrackerSettingBinding
    private lateinit var tvConfirm: TextView
    private lateinit var ivBack: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerSettingBinding.inflate(inflater, container, false)

        binding.tvConfirm.setOnClickListener{
            findNavController().navigate(R.id.action_settingsSleepTrackerFragment_to_homeSleepTrackerFragment)
            Toast.makeText(requireActivity(), "Dapat Data updated kaso wala pa (☉Д☉)!", Toast.LENGTH_SHORT).show()
        }
        binding.ivBack.setOnClickListener{
            findNavController().navigate(R.id.action_settingsSleepTrackerFragment_to_homeSleepTrackerFragment)
        }
        return binding.root
    }
}