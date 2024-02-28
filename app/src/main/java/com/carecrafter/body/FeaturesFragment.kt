package com.carecrafter.body

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carecrafter.body.features.BmiCalcuActivity
import com.carecrafter.body.features.DietSuggestionsActivity
import com.carecrafter.body.features.ExerciseSuggestionsActivity
import com.carecrafter.body.features.SleepTrackerActivity
import com.carecrafter.body.features.StepTrackerActivity
import com.carecrafter.body.features.WaterIntakeActivity
import com.carecrafter.body.features.water_intake.WaterIntakeBActivity
import com.carecrafter.databinding.BodyFeaturesBinding

class FeaturesFragment : Fragment() {
    private lateinit var binding: BodyFeaturesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BodyFeaturesBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming you have a button in your fragment layout to trigger the action

        binding.cvWaterIntake.setOnClickListener {
            // Open the FeatureActivity
            val intent = Intent(activity, WaterIntakeBActivity::class.java)
            startActivity(intent)
        }

        binding.cvStepTracker.setOnClickListener {
            // Open the FeatureActivity
            val intent = Intent(activity, StepTrackerActivity::class.java)
            startActivity(intent)
        }

        binding.cvSleepTracker.setOnClickListener {
            // Open the FeatureActivity
            val intent = Intent(activity, SleepTrackerActivity::class.java)
            startActivity(intent)
        }
        binding.cvBmiCalculator.setOnClickListener{
            // Open the FeatureActivity
            val intent = Intent(activity, BmiCalcuActivity::class.java)
            startActivity(intent)
        }

//        binding.cvExerciseSuggestion.setOnClickListener {
//            // Open the FeatureActivity
//            val intent = Intent(activity, ExerciseSuggestionsActivity::class.java)
//            startActivity(intent)
//        }

//        binding.cvDietSuggestion.setOnClickListener {
//            // Open the FeatureActivity
//            val intent = Intent(activity, DietSuggestionsActivity::class.java)
//            startActivity(intent)
//        }
    }
}