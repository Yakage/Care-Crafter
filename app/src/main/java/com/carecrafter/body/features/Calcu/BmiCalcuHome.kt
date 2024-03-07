package com.carecrafter.body.features.Calcu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.BmiHomeBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.User
import com.carecrafter.retrofit_database.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader
import kotlin.math.pow

class BmiCalcuHome : Fragment() {

    private lateinit var binding: BmiHomeBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BmiHomeBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getUserInfo(it) }

        binding.btnCalcu.setOnClickListener{
            calculatedBMI(authToken.toString())
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), BodyActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun calculatedBMI(authToken: String) {
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
            binding.tvCalcuResult.text = "$bmiResult \n $bmiCategory"
            createBMIData(authToken,bmiResult, bmiCategory)
//            findNavController().navigate(BmiCalcuHomeDirections.actionBmiCalcuHome2ToBmiResult())

        } else {
            binding.tvCalcuResult.text = "Invalid Input"
        }
    }

    private fun getUserInfo(authToken: String) {
        ApiClient.instance.getUser("Bearer $authToken").enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userData = response.body()
                    if (userData != null) {
                        updateInfo(userData)
                    }

                    val responseBody = response.body().toString()
                    Log.d("Response", responseBody)
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("AccountFragment", "Failed to get user info", t)
                Toast.makeText(requireContext(), "Failed to get user info", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun updateInfo(userData: User){
        if (userData != null) {
            binding.etWeightCalcu.text = Editable.Factory.getInstance().newEditable(userData.weight)
            binding.etHeightCalcu.text = Editable.Factory.getInstance().newEditable(userData.height)
            if (userData.gender == "male"){
                val colorStateList = ContextCompat.getColorStateList(requireContext(),  R.color.grey)
                binding.femaleIcon.imageTintList = (colorStateList)
                binding.maleIcon.setImageTintList(null);
            }
            else if (userData.gender == "female"){
                val colorStateList = ContextCompat.getColorStateList(requireContext(),  R.color.grey)
                binding.maleIcon.imageTintList = (colorStateList)
                binding.femaleIcon.setImageTintList(null);
            }
        }
    }

    private fun createBMIData(authToken: String, bmi: String, category: String){
        val createBMIDataJson =
            "{\"authToken\":\"$authToken\",\"bmi\":\"$bmi\",\"category\":\"$category\"}"

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(createBMIDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.createBMI(
                "Bearer $authToken",
                bmi,
                category
            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                        } else {
                            val errorMessage: String = try {
                                response.errorBody()?.string()
                                    ?: "Failed to get a valid response. Response code: ${response.code()}"
                            } catch (e: Exception) {
                                "Failed to get a valid response. Response code: ${response.code()}"
                            }
                            Toast.makeText(
                                requireContext(),
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.e("API_RESPONSE", errorMessage)
                        }
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }

}