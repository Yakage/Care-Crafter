package com.carecrafter.body.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.AccountUpdateAccountBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.SharedPrefsViewModel
import com.carecrafter.models.User
import com.carecrafter.retrofit_database.ApiClient
import com.carecrafter.roomdatabase.UserDao
import com.carecrafter.sqlitedatabase.CareCrafterDatabaseHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class UpdateAccount : Fragment() {
    private lateinit var binding: AccountUpdateAccountBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AccountUpdateAccountBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("myPreference", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", "")
        authToken.toString().let { getUserInfo(it) }
        binding.backButton.setOnClickListener {
            findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
        }

        binding.btUpdate.setOnClickListener {
            updateUserData(authToken.toString())
        }

        return binding.root
    }
    private fun getUserInfo(authToken: String) {
        ApiClient.instance.getUser("Bearer$authToken").enqueue(object : Callback<User>{
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
            binding.FullNameET.hint = userData.name
            binding.BirthdayET.hint = userData.birthday
            binding.GenderET.hint = userData.gender
            binding.HeightET.hint = userData.height
            binding.WeightET.hint = userData.weight
        }
    }

    private fun updateUserData(authToken: String){
        val name = binding.FullNameET.text.toString().trim()
        val birthday = binding.BirthdayET.text.toString().trim()
        val height = binding.HeightET.text.toString().trim()
        val weight = binding.WeightET.text.toString().trim()
        val gender = binding.GenderET.text.toString().toLowerCase().trim()

        val updateUserDataJson =
            "{\"authToken\":\"$authToken\",\"name\":\"$name\",\"birthday\":\"$birthday\",\"gender\":\"$gender\",\"height\":\"$height\",\"weight\":\"$weight\"}"

        //validation
        if (name.isEmpty()) {
            binding.FullNameET.error = "Full Name required"
            binding.FullNameET.requestFocus()
        }

        if (birthday.toString().isEmpty()) {
            binding.BirthdayET.error = "Age required"
            binding.BirthdayET.requestFocus()
        }

        if (height.isEmpty()) {
            binding.HeightET.error = "Height required"
            binding.HeightET.requestFocus()
        }

        if (weight.isEmpty()) {
            binding.WeightET.error = "Weight required"
            binding.WeightET.requestFocus()
        }

        if (gender.isEmpty()) {
            binding.GenderET.error = "Gender required"
            binding.GenderET.requestFocus()
        }

        //correct malformed data
        try {
            val reader = JsonReader(StringReader(updateUserDataJson))
            reader.isLenient = true
            reader.beginObject()
            reader.close()
            ApiClient.instance.updateUser(
                "Bearer $authToken",
                name,
                birthday,
                gender,
                height,
                weight,
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
                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
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