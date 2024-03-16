package com.carecrafter.body.features.Calcu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.BmiResultBinding
import com.carecrafter.models.BMI
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BmiResult : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: BmiResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BmiResultBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getBMIInfo(it) }

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }
        binding.btreturn.setOnClickListener {
            findNavController().navigate(R.id.action_bmiResult3_to_bmiCalcuHome)
        }

        return binding.root
    }

    private fun testing(authToken: String) {
        val bmi = binding.bmitest.text.toString().toDouble()
        if (bmi < 5.99 ) {
            binding.levelRed.visibility = View.VISIBLE
            binding.levelYellow.visibility = View.GONE
            binding.levelGreen.visibility = View.GONE
        } else if (bmi in 6.00..18.49) {
            binding.levelRed.visibility = View.GONE
            binding.levelYellow.visibility = View.VISIBLE
            binding.levelGreen.visibility = View.GONE
        } else if (bmi in 18.50..25.00) {
            binding.levelRed.visibility = View.GONE
            binding.levelYellow.visibility = View.GONE
            binding.levelGreen.visibility = View.VISIBLE
        } else if (bmi in 25.01 .. 29.00){
            binding.levelRed.visibility = View.GONE
            binding.levelYellow.visibility = View.VISIBLE
            binding.levelGreen.visibility = View.GONE
        } else {
            binding.levelRed.visibility = View.VISIBLE
            binding.levelYellow.visibility = View.GONE
            binding.levelGreen.visibility = View.GONE
        }
    }

    private fun getBMIInfo(authToken: String) {
        ApiClient.instance.getBMI("Bearer $authToken").enqueue(object : Callback<BMI> {
            override fun onResponse(call: Call<BMI>, response: Response<BMI>) {
                if (response.isSuccessful) {
                    val bmiData = response.body()
                    if (bmiData != null) {
                        updateInfo(bmiData)
                    }
                    testing(authToken)
                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response

                }
            }
            override fun onFailure(call: Call<BMI>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
            }
        })
    }
    fun updateInfo(bmiData: BMI){
        if (bmiData != null) {
            binding.tvBmi.text = bmiData.bmi
            binding.bmitest.text = bmiData.bmi
            binding.tvCategory.text = "Your Weight is: ${bmiData.category}"
        }
    }
}