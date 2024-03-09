package com.carecrafter.body.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.databinding.AchievementsHomeBinding


class Achievements : Fragment() {
    private lateinit var binding: AchievementsHomeBinding
    private var progress = 0

    //Increment Checker
    var hasIncrementedWater1 = false
    var hasIncrementedWater2 = false
    var hasIncrementedWater3 = false
    var hasIncrementedWater4 = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AchievementsHomeBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener {
            findNavController().navigate(AchievementsDirections.actionAchievementsToAccountFragment())
        }

        //Descriptions Water Intake
        binding.achievementWater1.setOnClickListener {
            if (!hasIncrementedWater1) {
                binding.description.text = "Drink at least one time"
                progress += 10
                updatebar()
                hasIncrementedWater1 = true
            }
        }
        binding.achievementWater2.setOnClickListener {
            if (!hasIncrementedWater2) { // Add this check
                binding.description.text = "Drink at least 10,000ml"
                progress += 10
                updatebar()
                hasIncrementedWater2 = true
            }
        }
        binding.achievementWater3.setOnClickListener{
            if (!hasIncrementedWater3) { // Add this check
                binding.description.text = "Drink at least 100,000ml"
                progress += 10
                updatebar()
                hasIncrementedWater3 = true
            }
        }
        binding.achievementWater4.setOnClickListener{
            if (!hasIncrementedWater4) { // Add this check
                binding.description.text = "Drink at least 1,000,000ml"
                progress += 10
                updatebar()
                hasIncrementedWater4 = true
            }
        }

        //Descriptions Step Tracker
        binding.achievementStep1.setOnClickListener{
            binding.description.text = "Walk at least 100 steps"
        }
        binding.achievementStep2.setOnClickListener{
            binding.description.text = "Walk at least 1,000 steps"
        }
        binding.achievementStep3.setOnClickListener{
            binding.description.text = "Walk at least 10,000 steps"
        }
        binding.achievementStep4.setOnClickListener{
            binding.description.text = "Walk at least 100,000 steps"
        }

        //Descriptions Sleep Tracker
        binding.achievementSleep1.setOnClickListener{
            binding.description.text = "Sleep for a total of 1 min"
        }
        binding.achievementSleep2.setOnClickListener{
            binding.description.text = "Sleep for a total of 1 hr"
        }
        binding.achievementSleep3.setOnClickListener{
            binding.description.text = "Sleep for a total of x8 hrs"
        }
        binding.achievementSleep4.setOnClickListener{
            binding.description.text = "Sleep for a total of 24 hrs"
        }

        return binding.root
    }

    private fun updatebar(){
        binding.progressbar.progress = progress
    }
}