package com.carecrafter.body.profile

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.carecrafter.MainActivity
import com.carecrafter.R
import com.carecrafter.databinding.AccountUpdateAccountBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.models.UserViewModel
import com.carecrafter.registration.SignIn
import com.carecrafter.retrofit_database.ApiClient
import com.carecrafter.roomdatabase.AppDatabase
import com.carecrafter.roomdatabase.User
import com.carecrafter.roomdatabase.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class UpdateAccount : Fragment() {
    private lateinit var binding: AccountUpdateAccountBinding
    private lateinit var userDao: UserDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = AccountUpdateAccountBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            findNavController().navigate(UpdateAccountDirections.actionUpdateAccountToAccountFragment())
        }


        binding.btUpdate.setOnClickListener {
            val name = binding.FullNameET.text.toString().trim()
            val email = binding.EmailET.text.toString().trim()
            val age = binding.AgeET.text.toString().trim()
            val height = binding.HeightET.text.toString().trim()
            val weight = binding.WeightET.text.toString().trim()
            val gender = binding.GenderET.text.toString().toLowerCase().trim()

            lifecycleScope.launch(Dispatchers.IO) {
                val user: User? = userDao.getUser()

                user?.let { loggedInUser ->
                    // userId retrieved successfully
                    val userId = loggedInUser.userId.toInt()

                    //json data
                    val updateUserDataJson =
                        "{\"userId\":\"$userId\",\"name\":\"$name\",\"email\":\"$email\",\"age\":\"$age\",\"height\":\"$height\",\"weight\":\"$weight\",\"gender\":\"$gender\"}"

                    //validation
                    if (name.isEmpty()) {
                        binding.FullNameET.error = "Full Name required"
                        binding.FullNameET.requestFocus()
                    }

                    if (email.isEmpty()) {
                        binding.EmailET.error = "Email required"
                        binding.EmailET.requestFocus()
                    }

                    if (age.toString().isEmpty()) {
                        binding.AgeET.error = "Age required"
                        binding.AgeET.requestFocus()
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
                            userId,
                            name,
                            email,
                            age,
                            height,
                            weight,
                            gender,
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
        }
        return binding.root
    }
}