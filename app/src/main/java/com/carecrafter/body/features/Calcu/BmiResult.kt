package com.carecrafter.body.features.Calcu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.BmiResultBinding

class BmiResult : Fragment() {

    private lateinit var binding: BmiResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BmiResultBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.bmi_result, container, false)

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}