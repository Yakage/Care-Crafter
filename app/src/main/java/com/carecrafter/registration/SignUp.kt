package com.carecrafter.registration

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carecrafter.databinding.RegistrationSignUpBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader


class SignUp : AppCompatActivity() {
    private lateinit var binding: RegistrationSignUpBinding
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Spinner Test

        binding = RegistrationSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        val genderSpinner : Spinner = binding.spinnerGender
        val genderIdentities = arrayOf(
            "Male",
            "Female",
        )

        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderIdentities)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Gender data
                val selectedGender = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btSubmit.setOnClickListener {
            binding.btSubmit.isClickable = false
            val selectedGender = genderSpinner.selectedItem.toString()
            val name = binding.FullNameET.text.toString().trim()
            val email = binding.EmailET.text.toString().trim()
            val birthday = binding.BirthdayET.text.toString().trim()
            val height = binding.HeightET.text.toString().trim()
            val weight = binding.WeightET.text.toString().trim()
            val password = binding.PasswordET.text.toString().trim()
            val confirmPassword = binding.ConfirmPasswordET.text.toString().trim()
            var gender = ""
            when (selectedGender) {
                "Male" -> gender = "male"
                "Female" -> gender = "female"
            }

            // Validation
            if (name.isEmpty()) {
                binding.FullNameET.error = "Full Name is required"
                binding.FullNameET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (email.isEmpty()) {
                binding.EmailET.error = "Email is required"
                binding.EmailET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (birthday.isEmpty()) {
                binding.BirthdayET.error = "Age is required"
                binding.BirthdayET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (height.isEmpty()) {
                binding.HeightET.error = "Height is required"
                binding.HeightET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (weight.isEmpty()) {
                binding.WeightET.error = "Weight is required"
                binding.WeightET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (password.isEmpty()) {
                binding.PasswordET.error = "Password is required"
                binding.PasswordET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            if (confirmPassword.isEmpty()) {
                binding.ConfirmPasswordET.error = "Confirm Password is required"
                binding.ConfirmPasswordET.requestFocus()
                return@setOnClickListener
                binding.btSubmit.isClickable = true
            }

            // Make API call
            ApiClient.instance.createUser(
                name,
                email,
                birthday,
                height,
                weight,
                gender,
                password,
                confirmPassword
            ).enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message ?: "Failed to make request", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        binding.btSubmit.isClickable = true
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@SignUp, SignIn::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        binding.btSubmit.isClickable = true
                        var errorMessage = response.errorBody()?.string() ?: "Failed to get a valid response"
                        // Extract message part from the error JSON
                        val messageRegex = """"message":"([^"]+)""".toRegex()
                        val matchResult = messageRegex.find(errorMessage)
                        val message = matchResult?.groupValues?.getOrNull(1)?.trim() ?: "Unknown error"
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        Log.e("API_RESPONSE", errorMessage)
                    }
                }
            })
        }
    }

}