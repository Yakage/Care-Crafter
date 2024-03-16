package com.carecrafter.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.carecrafter.R

class Welcome : AppCompatActivity() {

    private lateinit var welcomeLogin: Button
    private lateinit var welcomeSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeLogin = findViewById(R.id.welcomeLogin)
        welcomeSignup = findViewById(R.id.welcomeSignup)

        welcomeLogin.setOnClickListener {
            welcomeLogin.isClickable = false
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()
        }

        welcomeSignup.setOnClickListener {
            welcomeSignup.isClickable = false
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}
