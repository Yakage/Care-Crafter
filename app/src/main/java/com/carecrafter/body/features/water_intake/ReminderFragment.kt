package com.carecrafter.body.features.water_intake

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.carecrafter.R
import com.carecrafter.databinding.WaterIntakeReminderBinding


class ReminderFragment : Fragment() {
    private lateinit var binding: WaterIntakeReminderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = WaterIntakeReminderBinding.inflate(inflater, container, false)
        binding.toggleButtonCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Handle toggle ON state
                // Example: Do something when the switch is turned ON
            } else {
                // Handle toggle OFF state
                // Example: Do something when the switch is turned OFF
            }
        }
        binding.toggleButtonCompat2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Handle toggle ON state
                // Example: Do something when the switch is turned ON
            } else {
                // Handle toggle OFF state
                // Example: Do something when the switch is turned OFF
            }
        }
        binding.toggleButtonCompat3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Handle toggle ON state
                // Example: Do something when the switch is turned ON
            } else {
                // Handle toggle OFF state
                // Example: Do something when the switch is turned OFF
            }
        }
        binding.toggleButtonCompat4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Handle toggle ON state
                // Example: Do something when the switch is turned ON
            } else {
                // Handle toggle OFF state
                // Example: Do something when the switch is turned OFF
            }
        }
        binding.toggleButtonCompat5.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Handle toggle ON state
                // Example: Do something when the switch is turned ON
            } else {
                // Handle toggle OFF state
                // Example: Do something when the switch is turned OFF
            }
        }

        return binding.root



    }


}