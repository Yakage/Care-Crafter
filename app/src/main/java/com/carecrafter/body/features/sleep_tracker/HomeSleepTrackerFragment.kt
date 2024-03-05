package com.carecrafter.body.features.sleep_tracker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.SleepTrackerHomeBinding

class HomeSleepTrackerFragment : Fragment() {

    private lateinit var binding: SleepTrackerHomeBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SleepTrackerHomeBinding.inflate(inflater, container, false)

        binding.setGoalBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeSleepTrackerFragment_to_goalSetSleepTrackerFragment)
        }
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
