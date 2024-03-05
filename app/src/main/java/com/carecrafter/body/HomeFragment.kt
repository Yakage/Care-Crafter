package com.carecrafter.body

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carecrafter.body.features.BmiCalcuActivity
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.body.features.StepTrackerActivity
import com.carecrafter.body.features.water_intake.WaterIntakeBActivity
import com.carecrafter.databinding.BodyHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: BodyHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BodyHomeBinding.inflate(inflater, container, false)

        binding.sleepFT.setOnClickListener {
            val intent = Intent(activity, SleepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.stepFT.setOnClickListener {
            val intent = Intent(activity, StepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.waterFT.setOnClickListener {
            val intent = Intent(activity, WaterIntakeBActivity::class.java)
            startActivity(intent)
        }
        binding.bmiFT.setOnClickListener {
            val intent = Intent(activity, BmiCalcuActivity::class.java)
            startActivity(intent)
        }

        binding.btnVisitWebsite.setOnClickListener {
            val url = "https://example.com" //put the fuckining website here
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        return binding.root
    }
}
