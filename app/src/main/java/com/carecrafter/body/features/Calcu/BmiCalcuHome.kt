package com.carecrafter.body.features.Calcu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carecrafter.databinding.FragmentBmiCalcuHomeBinding
import kotlin.math.pow

class BmiCalcuHome : Fragment() {

    private lateinit var binding: FragmentBmiCalcuHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiCalcuHomeBinding.inflate(inflater, container, false)

        binding.btnCalcu.setOnClickListener{
            calculatedBMI()
        }

        return binding.root
    }

    private fun calculatedBMI() {
        val weight = binding.etWeightCalcu.text.toString().toFloatOrNull() //toFloatOrNull to prevent crash
        val height = binding.etHeightCalcu.text.toString().toFloatOrNull()

        if (weight != null && height != null){
            val bmi = weight/ (height/100).pow(2)
            val bmiResult = String.format("%.2f", bmi)

            val bmiCategory = when {
                bmi < 5 -> "Are you even a human my guy"
                bmi < 18.5 -> "Under Weight"
                bmi < 25 -> "Normal Weight"
                bmi < 30 -> "Over Weight"
                else -> "Obese"
            }
            binding.tvCalcuResult.text = "BMI: $bmiResult\nCategory: $bmiCategory"

            val Nah = binding.imageUgoodbro
            val Under = binding.imageUnder
            val Normal = binding.imageNormal
            val Over = binding.imageOver
            val Obese = binding.imagObese

            if (bmi <= 5) {
                Nah.visibility = View.VISIBLE
                Under.visibility = View.INVISIBLE
                Normal.visibility = View.INVISIBLE
                Over.visibility = View.INVISIBLE
                Obese.visibility = View.INVISIBLE
            }
            else if (bmi <= 18.5 && bmi < 25) {
                Nah.visibility = View.INVISIBLE
                Under.visibility = View.VISIBLE
                Normal.visibility = View.INVISIBLE
                Over.visibility = View.INVISIBLE
                Obese.visibility = View.INVISIBLE
            }
            else if (bmi <= 25 && bmi < 30) {
                Nah.visibility = View.INVISIBLE
                Under.visibility = View.INVISIBLE
                Normal.visibility = View.VISIBLE
                Over.visibility = View.INVISIBLE
                Obese.visibility = View.INVISIBLE
            }
            else if (bmi > 25 && bmi < 30) {
                Nah.visibility = View.INVISIBLE
                Under.visibility = View.INVISIBLE
                Normal.visibility = View.INVISIBLE
                Over.visibility = View.VISIBLE
                Obese.visibility = View.INVISIBLE
            }
            else if (bmi > 30) {
                Nah.visibility = View.INVISIBLE
                Under.visibility = View.INVISIBLE
                Normal.visibility = View.INVISIBLE
                Over.visibility = View.INVISIBLE
                Obese.visibility = View.VISIBLE
            }
        } else {
            binding.tvCalcuResult.text = "Invalid Input"
        }
    }

}