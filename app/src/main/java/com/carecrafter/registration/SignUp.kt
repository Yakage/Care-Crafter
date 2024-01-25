package com.carecrafter.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carecrafter.R
import com.carecrafter.databinding.RegistrationSignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: RegistrationSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSubmit.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
        binding.tvSignIn.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }
}