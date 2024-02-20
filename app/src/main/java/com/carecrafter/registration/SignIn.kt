package com.carecrafter.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.Toast
import com.carecrafter.body.BodyActivity
import com.carecrafter.databinding.RegistrationSignInBinding
import com.carecrafter.models.DefaultResponse
import com.carecrafter.retrofit_database.ApiClient
import com.carecrafter.userInputs.UserInputs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.StringReader

class SignIn : AppCompatActivity() {
    private lateinit var binding: RegistrationSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener {
            val email = binding.EmailET.text.toString().trim()
            val password = binding.PasswordET.text.toString().trim()

            val signinDataJson =
                "{\"email\":\"$email\",\"password\":\"$password\"}"

            if (email.isEmpty()) {
                binding.EmailET.error = "Email required"
                binding.EmailET.requestFocus()
            }

            if (password.toString().isEmpty()) {
                binding.PasswordET.error = "Password required"
                binding.PasswordET.requestFocus()
            }
            try {
                val reader = JsonReader(StringReader(signinDataJson))
                reader.isLenient = true
                reader.beginObject()
                reader.close()
                ApiClient.instance.loginUser(
                    email,
                    password
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
                                startActivity(
                                    Intent(this@SignIn, BodyActivity::class.java)
                                )
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

        binding.btSignup.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
