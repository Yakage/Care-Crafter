package com.carecrafter.body.features.water_intake

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.R
import com.carecrafter.databinding.WaterIntakeHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: WaterIntakeHomeBinding
    private var count = 0f
    private var total = 100f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = WaterIntakeHomeBinding.inflate(inflater, container, false)
        binding.tvCount.text = count.toString()
        binding.tvTotal.text = total.toString() + " oz"
        binding.circularProgressBar.apply {
            progressMax = total
            setProgressWithAnimation(00f, 1000)
            progressBarWidth = 5f
            backgroundProgressBarWidth = 7f
            progressBarColor = Color.WHITE
        }
        binding.layoutCircleProgressBar.setOnClickListener {
            count = count + 10f
            binding.circularProgressBar.apply {
                setProgressWithAnimation(count, 1000)
            }
            binding.tvCount.text = count.toString()
        }
        return binding.root
    }


}