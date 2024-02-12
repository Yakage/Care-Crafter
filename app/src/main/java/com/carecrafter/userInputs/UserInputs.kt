package com.carecrafter.userInputs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.carecrafter.R
import com.carecrafter.body.BodyActivity
import com.carecrafter.registration.SignUp

class UserInputs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_inputs)

        // id's
        val timeSpinner = findViewById<Spinner>(R.id.timeSpinnerOne)
        val finishButton = findViewById<Button>(R.id.finishButton)

        // array nako
        val timeOptions = arrayOf("4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", "8:00 AM",
            "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM",
            "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM", "12:00 AM",
            "1:00 AM", "2:00 AM", "3:00 AM")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timeOptions)

        // dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter
        timeSpinner.adapter = adapter

        // Set listener to handle the selected time
        timeSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedTime = timeOptions[position]
                // functions sa baba
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })

        finishButton.setOnClickListener {
            val intent = Intent(this, BodyActivity::class.java)
            startActivity(intent)
        }
    }
}
