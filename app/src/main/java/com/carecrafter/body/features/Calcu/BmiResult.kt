package com.carecrafter.body.features.Calcu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.BmiResultBinding
import com.carecrafter.models.BMI
import com.carecrafter.models.User
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

        return binding.root
    }

    private fun getBMIInfo(authToken: String) {
        ApiClient.instance.getBMI("Bearer $authToken").enqueue(object : Callback<BMI> {
            override fun onResponse(call: Call<BMI>, response: Response<BMI>) {
                if (response.isSuccessful) {
                    val bmiData = response.body()
                    if (bmiData != null) {
                        updateInfo(bmiData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<BMI>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun updateInfo(bmiData: BMI){
        if (bmiData != null) {
            binding.tvBmi.text = bmiData.bmi
            binding.tvCategory.text = "Category: ${bmiData.category}"
        }
    }
}