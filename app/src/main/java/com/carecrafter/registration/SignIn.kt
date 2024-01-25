package com.carecrafter.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carecrafter.body.Home
import com.carecrafter.databinding.RegistrationSignInBinding

class SignIn : AppCompatActivity() {
    private lateinit var binding: RegistrationSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegistrationSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener {
            val intent = Intent(this@SignIn, Home::class.java)
            startActivity(intent)
        }

        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this@SignIn, SignUp::class.java)
            startActivity(intent)
        }
    }
}