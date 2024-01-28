package com.carecrafter.body.features.water_intake

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.R
import com.carecrafter.databinding.WaterIntakeHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: WaterIntakeHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = WaterIntakeHomeBinding.inflate(inflater, container, false)


        return binding.root
    }


}