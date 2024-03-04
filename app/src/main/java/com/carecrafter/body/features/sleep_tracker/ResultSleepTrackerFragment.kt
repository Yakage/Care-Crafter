package com.carecrafter.body.features.sleep_tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carecrafter.databinding.FragmentResultSleepTrackerBinding


class ResultSleepTrackerFragment : Fragment() {
    private lateinit var binding: FragmentResultSleepTrackerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultSleepTrackerBinding.inflate(inflater, container, false)

        return binding.root
    }
}