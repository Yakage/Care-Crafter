package com.carecrafter.body.features.Calcu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carecrafter.R
import com.carecrafter.databinding.FragmentBmiResultBinding

class BmiResult : Fragment() {

    private lateinit var binding: FragmentBmiResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiResultBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_bmi_result, container, false)


        return binding.root
    }
}