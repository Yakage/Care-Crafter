package com.carecrafter.body

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.R
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.body.features.StepTrackerActivity
import com.carecrafter.body.features.WaterIntakeActivity
import com.carecrafter.databinding.BodyHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: BodyHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BodyHomeBinding.inflate(inflater, container, false)

        binding.llSleepTracker.setOnClickListener {
            val intent = Intent(activity, SleepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.llStepTracker.setOnClickListener {
            val intent = Intent(activity, StepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.llWaterIntake.setOnClickListener {
            val intent = Intent(activity, WaterIntakeActivity::class.java)
            startActivity(intent)
        }
//        binding.ivProfilePic.setOnClickListener {
//            val intent = Intent(activity, )
//        }

        return binding.root
    }


}