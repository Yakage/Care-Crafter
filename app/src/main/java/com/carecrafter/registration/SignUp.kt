package com.carecrafter.registration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.carecrafter.R
import com.carecrafter.databinding.RegistrationSignUpBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import com.carecrafter.retrofit_database.CareCrafterInterfaces
import java.io.StringReader
import android.util.JsonReader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SignUp : AppCompatActivity() {
    private lateinit var binding: RegistrationSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.btSubmit.setOnClickListener {
            val name = binding.FullNameET.text.toString().trim()
            val email = binding.EmailET.text.toString().trim()
            val age = binding.AgeET.text.toString().trim()
            val height = binding.HeightET.text.toString().trim()
            val weight = binding.WeightET.text.toString().trim()
            val gender = binding.GenderET.text.toString().trim()
            val password = binding.PasswordET.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordET.text.toString().trim()

            //json data
            val signupDataJson =
                "{\"name\":\"$name\",\"email\":\"$email\",\"age\":\"$age\",\"height\":\"$height\",\"weight\":\"$weight\",\"gender\":\"$gender\",\"password\":\"$password\",\"confirm_password\":\"$confirmPassword\"}"

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

            if (password.isEmpty()) {
                binding.PasswordET.error = "Email required"
                binding.PasswordET.requestFocus()
            }
            if (confirmPassword.isEmpty()) {
                binding.ConfirmPasswordET.error = "Email required"
                binding.ConfirmPasswordET.requestFocus()
            }

            //correct malformed data
            try {
                val reader = JsonReader(StringReader(signupDataJson))
                reader.isLenient = true
                reader.beginObject()
                reader.close()
                ApiClient.instance.createUser(
                    name,
                    email,
                    age,
                    height,
                    weight,
                    gender,
                    password,
                    confirmPassword,
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                Toast.makeText(
                                    applicationContext,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this@SignUp, SignIn::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                val errorMessage: String = try {
                                    response.errorBody()?.string()
                                        ?: "Failed to get a valid response. Response code: ${response.code()}"
                                } catch (e: Exception) {
                                    "Failed to get a valid response. Response code: ${response.code()}"
                                }
                                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG)
                                    .show()
                                Log.e("API_RESPONSE", errorMessage)
                            }
                        }
                    })
            } catch (e: Exception) {
                Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}