package com.carecrafter.body

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.R
import com.carecrafter.databinding.BodyDashboardBinding
import com.carecrafter.databinding.BodyHomeBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: BodyDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = BodyDashboardBinding.inflate(inflater, container, false)


        return binding.root
    }
}